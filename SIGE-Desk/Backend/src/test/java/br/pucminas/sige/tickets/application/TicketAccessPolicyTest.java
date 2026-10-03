package br.pucminas.sige.tickets.application;

import static org.junit.jupiter.api.Assertions.*;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketRepository;
import br.pucminas.sige.users.domain.AppUser;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TicketAccessPolicyTest {
  private TicketAccessPolicy policy;
  private Ticket ticket;
  private AppUser owner;
  private AppUser anotherClient;
  private AppUser assignedManager;

  @BeforeEach
  void setUp() {
    policy = new TicketAccessPolicy(Mockito.mock(TicketRepository.class));
    Client aurora = new Client("Aurora", "Marina", "aurora@example.test", null);
    Client horizonte = new Client("Horizonte", "Rafael", "horizonte@example.test", null);
    owner = new AppUser("Marina", "marina@example.test", "hash", Role.CLIENT, aurora);
    anotherClient = new AppUser("Rafael", "rafael@example.test", "hash", Role.CLIENT, horizonte);
    assignedManager = new AppUser("Lucas", "lucas@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    DemandType type = new DemandType("Ajuste", false, false, "[]");
    ticket = new Ticket("SIGE-2000", aurora, null, "Campanha", type, "Google Ads", "Assunto", "Descrição", Priority.MEDIUM, LocalDate.now(), owner, owner, "{}");
    ticket.startTriage();
    ticket.classify(Priority.MEDIUM, assignedManager, "{}", java.time.Instant.now(), java.time.Instant.now());
    ticket.sendToExecution();
  }

  @Test
  void clientCanOnlyAccessTicketsFromOwnOrganization() {
    assertTrue(policy.canAccess(ticket, owner));
    assertFalse(policy.canAccess(ticket, anotherClient));
  }

  @Test
  void trafficManagerCanOnlyAccessAssignedTicket() {
    AppUser unassigned = new AppUser("Carla", "carla@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    assertTrue(policy.canAccess(ticket, assignedManager));
    assertFalse(policy.canAccess(ticket, unassigned));
  }

  @Test
  void serviceAndAdminCanAccessAgencyScope() {
    AppUser service = new AppUser("Bia", "bia@example.test", "hash", Role.SERVICE, null);
    AppUser admin = new AppUser("Nayara", "admin@example.test", "hash", Role.ADMIN, null);
    assertTrue(policy.canAccess(ticket, service));
    assertTrue(policy.canAccess(ticket, admin));
  }
}
