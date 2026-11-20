package br.pucminas.sige.tickets.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.pucminas.sige.tickets.application.TicketCreationService;
import br.pucminas.sige.tickets.application.TicketService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters=false)
@Import(TicketVersionTransactionTest.TransactionConfig.class)
class TicketVersionTransactionTest {
  @Autowired MockMvc mvc;
  @Autowired TrackingTransactionManager transactions;
  @MockitoBean TicketService service;
  @MockitoBean TicketCreationService creation;
  @MockitoBean br.pucminas.sige.shared.security.SecurityAccessAudit securityAudit;

  @Test void deniedExecutionRollsBackVersionCheckWithAction() throws Exception {
    UUID id=UUID.randomUUID();
    doAnswer(invocation -> { assertTrue(TransactionSynchronizationManager.isActualTransactionActive()); return null; })
        .when(service).verifyVersion(id,"6");
    when(service.execute(eq(id),any())).thenThrow(new AccessDeniedException("Ação não permitida"));
    int before=transactions.rollbacks;
    mvc.perform(post("/api/v1/tickets/{id}/execution",id).header("If-Match","6")
        .contentType("application/json").content("{\"actionDescription\":\"teste\"}"))
        .andExpect(status().isForbidden());
    assertEquals(before+1,transactions.rollbacks);
    var order=inOrder(service);
    order.verify(service).verifyVersion(id,"6");
    order.verify(service).execute(eq(id),any());
  }

  @Test void concurrentDatabaseLockReturnsConflictAndRollsBack() throws Exception {
    UUID id=UUID.randomUUID();
    doThrow(new org.springframework.dao.CannotAcquireLockException("deadlock"))
        .when(service).verifyVersion(id,"6");
    int before=transactions.rollbacks;
    mvc.perform(post("/api/v1/tickets/{id}/comments",id).header("If-Match","6")
        .contentType("application/json").content("{\"body\":\"teste\",\"effectiveResponse\":false}"))
        .andExpect(status().isConflict())
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.code").value("CONCURRENT_OPERATION"));
    assertEquals(before+1,transactions.rollbacks);
    verify(service,never()).comment(any(),any());
  }
  @Test void invalidBodyDoesNotStartVersionCheck() throws Exception {
    UUID id=UUID.randomUUID();
    mvc.perform(post("/api/v1/tickets/{id}/execution",id).header("If-Match","6")
        .contentType("application/json").content("{}"))
        .andExpect(status().isBadRequest());
    verify(service).verifyVisibility(id);
    verify(service,never()).verifyVersion(any(),any());
  }

  @Test void hiddenTicketIsRejectedBeforeInvalidBodyIsValidated() throws Exception {
    UUID id=UUID.randomUUID();
    doThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Ticket não encontrado"))
        .when(service).verifyVisibility(id);
    mvc.perform(post("/api/v1/tickets/{id}/comments",id).header("If-Match","6")
        .contentType("application/json").content("{\"body\":\"\"}"))
        .andExpect(status().isNotFound());
    verify(service,never()).verifyVersion(any(),any());
    verify(service,never()).comment(any(),any());
  }

  @TestConfiguration @EnableTransactionManagement
  static class TransactionConfig {
    @Bean TrackingTransactionManager transactionManager(){return new TrackingTransactionManager();}
  }
  static class TrackingTransactionManager extends AbstractPlatformTransactionManager {
    int rollbacks;
    protected Object doGetTransaction(){return new Object();}
    protected void doBegin(Object transaction,TransactionDefinition definition){}
    protected void doCommit(DefaultTransactionStatus status){}
    protected void doRollback(DefaultTransactionStatus status){rollbacks++;}
  }
}
