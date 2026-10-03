package br.pucminas.sige.notifications.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.notifications.domain.NotificationRepository;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.outbox.OutboxEvent;
import br.pucminas.sige.shared.outbox.OutboxEventRepository;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class NotificationServiceTest {
  @Test
  void createsOneNotificationPerRecipientAndOneOutboxEvent() {
    NotificationRepository notifications = Mockito.mock(NotificationRepository.class);
    AppUserRepository users = Mockito.mock(AppUserRepository.class);
    OutboxEventRepository outbox = Mockito.mock(OutboxEventRepository.class);
    Client client = new Client("Aurora", "Marina", "aurora@example.test", null);
    AppUser requester = new AppUser("Marina", "marina@example.test", "hash", Role.CLIENT, client);
    AppUser manager = new AppUser("Lucas", "lucas@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    AppUser service = new AppUser("Beatriz", "service@example.test", "hash", Role.SERVICE, null);
    AppUser admin = new AppUser("Nayara", "admin@example.test", "hash", Role.ADMIN, null);
    Ticket ticket = new Ticket("SIGE-7001", client, null, "Campanha", new DemandType("Ajuste", false, false, "[]"),
        "Google Ads", "Assunto", "Descrição", Priority.MEDIUM, LocalDate.now(), requester, requester, "{}");
    ticket.startTriage();
    ticket.classify(Priority.MEDIUM, manager, "{}", java.time.Instant.now(), java.time.Instant.now());
    when(users.findByRoleInAndActiveTrue(List.of(Role.SERVICE, Role.ADMIN))).thenReturn(List.of(service, admin));
    for (AppUser user : List.of(requester, manager, service, admin)) when(users.findById(user.getId())).thenReturn(Optional.of(user));

    new NotificationService(notifications, users, outbox).publish(ticket, "ASSIGNED", "Ticket atribuído");

    verify(notifications, times(4)).save(any());
    ArgumentCaptor<OutboxEvent> event = ArgumentCaptor.forClass(OutboxEvent.class);
    verify(outbox).save(event.capture());
    org.junit.jupiter.api.Assertions.assertEquals("ASSIGNED", event.getValue().getEventType());
    org.junit.jupiter.api.Assertions.assertTrue(event.getValue().getPayload().contains(ticket.getId().toString()));
  }
}
