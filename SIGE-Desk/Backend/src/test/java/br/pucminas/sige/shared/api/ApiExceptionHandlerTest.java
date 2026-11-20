package br.pucminas.sige.shared.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiExceptionHandlerTest {
  @Test
  void auditsDeniedTicketBeforeReturningGenericNotFound() {
    var audit = org.mockito.Mockito.mock(br.pucminas.sige.shared.security.SecurityAccessAudit.class);
    var actor = org.mockito.Mockito.mock(br.pucminas.sige.users.domain.AppUser.class);
    var ticketId = java.util.UUID.randomUUID();
    var response = new ApiExceptionHandler(audit).ticketDenied(new br.pucminas.sige.shared.security.TicketAccessDeniedException(actor, ticketId));
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Ticket não encontrado", response.getBody().message());
    org.mockito.Mockito.verify(audit).ticketAccessDenied(actor, ticketId);
  }
  @Test
  void auditFailureReturnsSameGenericBodyAsMissingTicketAndLogsFailure() {
    var audit = org.mockito.Mockito.mock(br.pucminas.sige.shared.security.SecurityAccessAudit.class);
    var actor = org.mockito.Mockito.mock(br.pucminas.sige.users.domain.AppUser.class);
    var actorId = java.util.UUID.randomUUID();
    var ticketId = java.util.UUID.randomUUID();
    org.mockito.Mockito.when(actor.getId()).thenReturn(actorId);
    org.mockito.Mockito.doThrow(new org.springframework.dao.DataIntegrityViolationException("private database detail"))
      .when(audit).ticketAccessDenied(actor, ticketId);
    var logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ApiExceptionHandler.class);
    var appender = new ch.qos.logback.core.read.ListAppender<ch.qos.logback.classic.spi.ILoggingEvent>();
    appender.start(); logger.addAppender(appender);
    try {
      var handler = new ApiExceptionHandler(audit);
      var denied = handler.ticketDenied(new br.pucminas.sige.shared.security.TicketAccessDeniedException(actor, ticketId));
      var missing = handler.status(new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));
      assertEquals(missing.getStatusCode(), denied.getStatusCode());
      assertEquals(missing.getBody().code(), denied.getBody().code());
      assertEquals(missing.getBody().message(), denied.getBody().message());
      assertEquals(missing.getBody().fields(), denied.getBody().fields());
      org.junit.jupiter.api.Assertions.assertNotNull(denied.getBody().timestamp());
      org.junit.jupiter.api.Assertions.assertTrue(appender.list.stream().anyMatch(e -> e.getFormattedMessage().contains("SECURITY_TICKET_AUDIT_FAILED") && e.getFormattedMessage().contains(actorId.toString()) && e.getFormattedMessage().contains(ticketId.toString())));
      org.junit.jupiter.api.Assertions.assertTrue(appender.list.stream().noneMatch(e -> e.getFormattedMessage().contains("private database detail")));
    } finally { logger.detachAppender(appender); appender.stop(); }
  }
  @Test
  void returnsConflictCodeForOptimisticLocking() {
    var response = new ApiExceptionHandler(org.mockito.Mockito.mock(br.pucminas.sige.shared.security.SecurityAccessAudit.class)).optimistic(new OptimisticLockException());

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("VERSION_CONFLICT", response.getBody().code());
  }
}
