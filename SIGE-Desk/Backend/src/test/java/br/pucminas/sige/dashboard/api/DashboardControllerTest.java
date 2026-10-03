package br.pucminas.sige.dashboard.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.application.TicketAccessPolicy;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class DashboardControllerTest {
  @Test
  void countsOnlyActiveHighPriorityTickets() {
    Client client = new Client("Aurora", "Marina", "aurora@example.test", null);
    AppUser service = new AppUser("Beatriz", "service@example.test", "hash", Role.SERVICE, null);
    AppUser requester = new AppUser("Marina", "client@example.test", "hash", Role.CLIENT, client);
    Ticket active = classified("SIGE-6001", client, requester);
    Ticket completed = classified("SIGE-6002", client, requester);
    completed.sendToExecution();
    completed.recordExecution(false);
    Ticket cancelled = classified("SIGE-6003", client, requester);
    cancelled.cancel();
    TicketAccessPolicy access = Mockito.mock(TicketAccessPolicy.class);
    CurrentUser current = Mockito.mock(CurrentUser.class);
    when(current.require()).thenReturn(service);
    when(access.visibleTickets(service)).thenReturn(List.of(active, completed, cancelled));

    DashboardController.Response response = new DashboardController(access, current).dashboard();

    assertEquals(1, response.activeCount());
    assertEquals(1, response.highPriorityCount());
  }

  private Ticket classified(String number, Client client, AppUser requester) {
    Ticket ticket = new Ticket(number, client, null, "Campanha", new DemandType("Ajuste", false, false, "[]"),
        "Google Ads", "Assunto", "Descrição", Priority.HIGH, LocalDate.now(), requester, requester, "{}");
    Instant now = Instant.now();
    ReflectionTestUtils.setField(ticket, "createdAt", now);
    ReflectionTestUtils.setField(ticket, "updatedAt", now);
    ticket.startTriage();
    ticket.classify(Priority.HIGH, new AppUser("Lucas", "manager@example.test", "hash", Role.TRAFFIC_MANAGER, null), "{}", now.plusSeconds(3600), now.plusSeconds(7200));
    return ticket;
  }
}
