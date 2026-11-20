package br.pucminas.sige.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.sla.application.SlaService;
import br.pucminas.sige.tickets.application.TicketQuery;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketRepository;
import br.pucminas.sige.tickets.domain.WaitOrigin;
import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@Tag("mysql")
@EnabledIfEnvironmentVariable(named="SIGE_TEST_DB_URL", matches=".+")
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class TicketOverdueMySqlIntegrationTest {
  @DynamicPropertySource
  static void database(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", () -> System.getenv("SIGE_TEST_DB_URL"));
    registry.add("spring.datasource.username", () -> System.getenv("SIGE_TEST_DB_USERNAME"));
    registry.add("spring.datasource.password", () -> System.getenv("SIGE_TEST_DB_PASSWORD"));
  }

  @Autowired EntityManager entityManager;
  @Autowired TicketRepository tickets;

  @Test
  void filteredResultsMatchSlaStatesForEachAuthorizedRole() {
    Instant now=Instant.parse("2025-01-01T12:00:00Z");
    Client client=new Client("Teste SLA", "Contato", "sla@example.test", null);
    DemandType type=new DemandType("Teste SLA", false, false, "[]");
    AppUser requester=new AppUser("Cliente", "sla-client@example.test", "hash", Role.CLIENT, client);
    AppUser manager=new AppUser("Gestor", "sla-manager@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    AppUser service=new AppUser("Atendimento", "sla-service@example.test", "hash", Role.SERVICE, null);
    AppUser admin=new AppUser("Admin", "sla-admin@example.test", "hash", Role.ADMIN, null);
    for(Object entity:List.of(client,type,requester,manager,service,admin)) entityManager.persist(entity);

    Ticket pausedLate=ticket("SLA-1",client,type,requester,manager,now.minusSeconds(60),now.minusSeconds(120));
    pausedLate.waitForClient(WaitOrigin.TRIAGE,"Aguardando");
    Ticket pausedOnTime=ticket("SLA-2",client,type,requester,manager,now.plusSeconds(60),now.minusSeconds(120));
    pausedOnTime.waitForClient(WaitOrigin.TRIAGE,"Aguardando");
    Ticket lateResponse=ticket("SLA-3",client,type,requester,manager,now.minusSeconds(120),now.plusSeconds(60));
    ReflectionTestUtils.setField(lateResponse,"responseCompletedAt",now.minusSeconds(60));
    Ticket lateResolution=ticket("SLA-4",client,type,requester,manager,now.plusSeconds(60),now.minusSeconds(60));
    Ticket cancelled=ticket("SLA-5",client,type,requester,manager,now.minusSeconds(60),now.minusSeconds(60));
    cancelled.cancel();
    Ticket onTime=ticket("SLA-6",client,type,requester,manager,now.plusSeconds(60),now.plusSeconds(60));
    Ticket completedOnTime=ticket("SLA-7",client,type,requester,manager,now.minusSeconds(120),now.minusSeconds(120));
    ReflectionTestUtils.setField(completedOnTime,"responseCompletedAt",now.minusSeconds(180));
    ReflectionTestUtils.setField(completedOnTime,"completedAt",now.minusSeconds(180));
    Ticket responseDueNow=ticket("SLA-8",client,type,requester,manager,now,now.plusSeconds(60));
    Ticket resolutionDueNow=ticket("SLA-9",client,type,requester,manager,now.plusSeconds(60),now);
    Ticket completedAtDeadline=ticket("SLA-10",client,type,requester,manager,now.minusSeconds(60),now.minusSeconds(60));
    ReflectionTestUtils.setField(completedAtDeadline,"responseCompletedAt",now.minusSeconds(60));
    ReflectionTestUtils.setField(completedAtDeadline,"completedAt",now.minusSeconds(60));
    Ticket resolutionCompletedLate=ticket("SLA-11",client,type,requester,manager,now.plusSeconds(60),now.minusSeconds(120));
    ReflectionTestUtils.setField(resolutionCompletedLate,"completedAt",now.minusSeconds(60));
    List<Ticket> all=List.of(pausedLate,pausedOnTime,lateResponse,lateResolution,cancelled,onTime,completedOnTime,
        responseDueNow,resolutionDueNow,completedAtDeadline,resolutionCompletedLate);
    all.forEach(entityManager::persist);
    entityManager.flush();
    entityManager.clear();

    Set<String> expected=all.stream().filter(t -> t!=cancelled).filter(t -> "OVERDUE".equals(SlaService.responseState(t,now)) || "OVERDUE".equals(SlaService.resolutionState(t,now))).map(Ticket::getNumber).collect(Collectors.toSet());
    assertEquals(Set.of("SLA-1","SLA-3","SLA-4","SLA-11"),expected);
    for(AppUser user:List.of(requester,manager,service,admin)) {
      var filter=TicketQuery.filters(user,null,null,null,null,type.getId(),null,null,null,null,false).and(TicketQuery.overdue(now));
      assertEquals(expected,tickets.findAll(filter).stream().map(Ticket::getNumber).collect(Collectors.toSet()),user.getRole().name());
    }
  }

  private Ticket ticket(String number, Client client, DemandType type, AppUser requester, AppUser manager, Instant responseDue, Instant resolutionDue) {
    Ticket ticket=new Ticket(number,client,null,"Pendente",type,"Web","Assunto","Descrição",Priority.MEDIUM,null,requester,requester,"{}");
    ticket.startTriage();
    ticket.classify(Priority.MEDIUM,manager,"{}",responseDue,resolutionDue);
    return ticket;
  }
}
