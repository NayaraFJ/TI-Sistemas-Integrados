package br.pucminas.sige.sla.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.sla.domain.SlaRule;
import br.pucminas.sige.sla.domain.SlaRuleRepository;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class SlaServiceTest {
  private static final String DEADLINES = "{\"MEDIUM\":{\"responseHours\":2,\"resolutionHours\":10}}";

  @Test
  void calculatesOnlyInsideBusinessHours() {
    SlaRuleRepository repository = Mockito.mock(SlaRuleRepository.class);
    SlaRule rule = rule(SlaRule.Scope.DEFAULT, null, null, DEADLINES);
    when(repository.findByActiveTrue()).thenReturn(List.of(rule));

    SlaService.Calculation result = new SlaService(repository, new ObjectMapper()).calculate(ticket(), Priority.MEDIUM);

    assertEquals(Instant.parse("2026-09-28T15:00:00Z"), result.responseDueAt());
    assertEquals(Instant.parse("2026-09-29T13:00:00Z"), result.resolutionDueAt());
  }

  @Test
  void choosesTheMostSpecificActiveRule() {
    Ticket ticket = ticket();
    SlaRuleRepository repository = Mockito.mock(SlaRuleRepository.class);
    SlaRule fallback = rule(SlaRule.Scope.DEFAULT, null, null, DEADLINES);
    SlaRule specific = rule(SlaRule.Scope.CLIENT_AND_DEMAND_TYPE, ticket.getClient(), ticket.getDemandType(), "{\"MEDIUM\":{\"responseHours\":1,\"resolutionHours\":1}}");
    when(repository.findByActiveTrue()).thenReturn(List.of(fallback, specific));

    SlaService.Calculation result = new SlaService(repository, new ObjectMapper()).calculate(ticket, Priority.MEDIUM);

    assertEquals(Instant.parse("2026-09-28T14:00:00Z"), result.responseDueAt());
    assertEquals(Instant.parse("2026-09-28T14:00:00Z"), result.resolutionDueAt());
  }

  @Test
  @Timeout(value=2, threadMode=Timeout.ThreadMode.SEPARATE_THREAD)
  void preservesSecondsAndFractionsWhenCrossingTheEndOfABusinessDay() {
    Ticket ticket=ticket();
    ReflectionTestUtils.setField(ticket,"createdAt",Instant.parse("2026-09-28T20:59:30.123456Z"));
    SlaRuleRepository repository=Mockito.mock(SlaRuleRepository.class);
    when(repository.findByActiveTrue()).thenReturn(List.of(rule(SlaRule.Scope.DEFAULT,null,null,DEADLINES)));

    SlaService.Calculation result=new SlaService(repository,new ObjectMapper()).calculate(ticket,Priority.MEDIUM);

    assertEquals(Instant.parse("2026-09-29T12:59:30.123456Z"),result.responseDueAt());
    assertEquals(Instant.parse("2026-09-29T20:59:30.123456Z"),result.resolutionDueAt());
  }

  @Test void clientRuleTakesPrecedenceOverTypeRule() {
    Ticket ticket=ticket();var repository=Mockito.mock(SlaRuleRepository.class);
    var clientRule=rule(SlaRule.Scope.CLIENT,ticket.getClient(),null,"{\"MEDIUM\":{\"responseHours\":1,\"resolutionHours\":1}}");
    var typeRule=rule(SlaRule.Scope.DEMAND_TYPE,null,ticket.getDemandType(),DEADLINES);
    when(repository.findByActiveTrue()).thenReturn(List.of(clientRule,typeRule));
    assertEquals(Instant.parse("2026-09-28T14:00:00Z"),new SlaService(repository,new ObjectMapper()).calculate(ticket,Priority.MEDIUM).resolutionDueAt());
  }
  @Test void frozenCalendarSurvivesRuleEdits() {
    Ticket ticket=ticket();var repository=Mockito.mock(SlaRuleRepository.class);var rule=rule(SlaRule.Scope.DEFAULT,null,null,DEADLINES);
    when(repository.findByActiveTrue()).thenReturn(List.of(rule));var service=new SlaService(repository,new ObjectMapper());var initial=service.calculate(ticket,Priority.MEDIUM);
    ticket.startTriage();ticket.classify(Priority.MEDIUM,null,initial.snapshot(),initial.responseDueAt(),initial.resolutionDueAt());
    rule.update("Editada",SlaRule.Scope.DEFAULT,null,null,"UTC","[\"SATURDAY\"]",LocalTime.of(1,0),LocalTime.of(2,0),"[]",false,"{}",true);
    assertEquals(initial.resolutionDueAt(),service.calculate(ticket,Priority.MEDIUM).resolutionDueAt());
  }
  @Test void excludesOnlyBusinessTimeDuringComplementPause() {
    Ticket ticket=ticket();var repository=Mockito.mock(SlaRuleRepository.class);when(repository.findByActiveTrue()).thenReturn(List.of(rule(SlaRule.Scope.DEFAULT,null,null,DEADLINES)));
    var service=new SlaService(repository,new ObjectMapper());var initial=service.calculate(ticket,Priority.MEDIUM);ticket.startTriage();ticket.classify(Priority.MEDIUM,null,initial.snapshot(),initial.responseDueAt(),initial.resolutionDueAt());
    ReflectionTestUtils.setField(ticket,"resolutionPauseIntervals","[{\"start\":\"2026-09-28T15:00:00Z\",\"end\":\"2026-09-29T13:00:00Z\"}]");
    assertEquals(Instant.parse("2026-09-29T21:00:00Z"),service.calculate(ticket,Priority.MEDIUM).resolutionDueAt());
    assertEquals(initial.responseDueAt(),service.calculate(ticket,Priority.MEDIUM).responseDueAt());
  }
  @Test void newCycleStartsAtReopeningAndKeepsTheFirstResponseDeadline() {
    Ticket ticket=ticket();var repository=Mockito.mock(SlaRuleRepository.class);when(repository.findByActiveTrue()).thenReturn(List.of(rule(SlaRule.Scope.DEFAULT,null,null,DEADLINES)));var service=new SlaService(repository,new ObjectMapper());
    ReflectionTestUtils.setField(ticket,"resolutionCycle",2);ReflectionTestUtils.setField(ticket,"resolutionCycleStartedAt",Instant.parse("2026-10-05T11:00:00Z"));ReflectionTestUtils.setField(ticket,"responseCompletedAt",Instant.parse("2026-09-28T14:00:00Z"));ReflectionTestUtils.setField(ticket,"responseDueAt",Instant.parse("2026-09-28T15:00:00Z"));
    var result=service.calculate(ticket,Priority.MEDIUM);assertEquals(Instant.parse("2026-10-05T21:00:00Z"),result.resolutionDueAt());assertEquals(Instant.parse("2026-09-28T15:00:00Z"),result.responseDueAt());
  }
  @Test void showsWhichDeadlineExpiredEvenWhenResolutionIsPaused() {
    Ticket ticket=ticket();ReflectionTestUtils.setField(ticket,"responseDueAt",Instant.parse("2026-09-28T15:00:00Z"));ReflectionTestUtils.setField(ticket,"resolutionDueAt",Instant.parse("2026-09-29T13:00:00Z"));ReflectionTestUtils.setField(ticket,"resolutionPausedAt",Instant.parse("2026-09-28T16:00:00Z"));
    assertEquals("OVERDUE",SlaService.responseState(ticket,Instant.parse("2026-09-28T17:00:00Z")));assertEquals("PAUSED",SlaService.resolutionState(ticket,Instant.parse("2026-09-28T17:00:00Z")));
  }
  @Test @Timeout(value=2,threadMode=Timeout.ThreadMode.SEPARATE_THREAD) void emptyCalendarFailsInsteadOfLoopingForever() {
    var repository=Mockito.mock(SlaRuleRepository.class);var invalid=rule(SlaRule.Scope.DEFAULT,null,null,DEADLINES);ReflectionTestUtils.setField(invalid,"businessDays","[]");when(repository.findByActiveTrue()).thenReturn(List.of(invalid));
    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,()->new SlaService(repository,new ObjectMapper()).calculate(ticket(),Priority.MEDIUM));
  }

  private SlaRule rule(SlaRule.Scope scope, Client client, DemandType type, String deadlines) {
    SlaRule rule = new SlaRule("Regra", scope, client, type, deadlines);
    rule.update("Regra", scope, client, type, "America/Sao_Paulo", "[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\"]", LocalTime.of(8, 0), LocalTime.of(18, 0), "[]", false, deadlines, true);
    return rule;
  }

  private Ticket ticket() {
    Client client = new Client("Aurora", "Marina", "aurora@example.test", null);
    AppUser requester = new AppUser("Marina", "marina@example.test", "hash", Role.CLIENT, client);
    DemandType type = new DemandType("Ajuste", false, false, "[]");
    Ticket ticket = new Ticket("SIGE-3000", client, null, "Campanha", type, "Google Ads", "Assunto", "Descrição", Priority.MEDIUM, LocalDate.now(), requester, requester, "{}");
    ReflectionTestUtils.setField(ticket, "createdAt", Instant.parse("2026-09-28T13:00:00Z"));
    return ticket;
  }
}
