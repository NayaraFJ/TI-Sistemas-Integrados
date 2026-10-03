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
