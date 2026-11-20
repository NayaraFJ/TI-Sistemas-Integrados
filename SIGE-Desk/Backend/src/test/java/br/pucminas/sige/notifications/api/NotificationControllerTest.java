package br.pucminas.sige.notifications.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.notifications.domain.Notification;
import br.pucminas.sige.notifications.domain.NotificationRepository;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.application.TicketAccessPolicy;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NotificationControllerTest {
  @Test
  void hidesNotificationsWhenTheTicketIsOutsideTheCurrentScope() {
    NotificationRepository notifications = Mockito.mock(NotificationRepository.class);
    CurrentUser current = Mockito.mock(CurrentUser.class);
    TicketAccessPolicy access = Mockito.mock(TicketAccessPolicy.class);
    AppUser user = user();
    Ticket ticket = ticket(user);
    Notification notification = new Notification(user, ticket, "ASSIGNED", "Novo responsável");
    when(current.require()).thenReturn(user);
    when(notifications.findById(notification.getId())).thenReturn(java.util.Optional.of(notification));
    when(access.canAccess(ticket, user)).thenReturn(false);

    var ex=org.junit.jupiter.api.Assertions.assertThrows(org.springframework.web.server.ResponseStatusException.class,()->new NotificationController(notifications,current,access).markRead(notification.getId()));
    assertEquals(404,ex.getStatusCode().value());
    org.junit.jupiter.api.Assertions.assertInstanceOf(br.pucminas.sige.shared.security.LinkedResourceAccessDeniedException.class, ex);
    org.junit.jupiter.api.Assertions.assertNull(notification.getReadAt());
  }

  @Test
  void distinguishesAnAbsentNotificationInternallyWithoutChangingTheResponse() {
    NotificationRepository notifications = Mockito.mock(NotificationRepository.class);
    CurrentUser current = Mockito.mock(CurrentUser.class);
    TicketAccessPolicy access = Mockito.mock(TicketAccessPolicy.class);
    var id = java.util.UUID.randomUUID();
    when(current.require()).thenReturn(user());
    when(notifications.findById(id)).thenReturn(java.util.Optional.empty());
    var ex = org.junit.jupiter.api.Assertions.assertThrows(org.springframework.web.server.ResponseStatusException.class,
        () -> new NotificationController(notifications, current, access).markRead(id));
    assertEquals(404, ex.getStatusCode().value());
    org.junit.jupiter.api.Assertions.assertEquals(org.springframework.web.server.ResponseStatusException.class, ex.getClass());
  }

  @Test
  void deniesAnotherUsersNotificationEvenWhenTicketIsVisible() {
    NotificationRepository notifications = Mockito.mock(NotificationRepository.class);
    CurrentUser current = Mockito.mock(CurrentUser.class);
    TicketAccessPolicy access = Mockito.mock(TicketAccessPolicy.class);
    AppUser owner = user();
    AppUser actor = user();
    Ticket ticket = ticket(owner);
    Notification notification = new Notification(owner, ticket, "ASSIGNED", "Resumo");
    when(current.require()).thenReturn(actor);
    when(notifications.findById(notification.getId())).thenReturn(java.util.Optional.of(notification));
    var ex = org.junit.jupiter.api.Assertions.assertThrows(br.pucminas.sige.shared.security.LinkedResourceAccessDeniedException.class,
        () -> new NotificationController(notifications, current, access).markRead(notification.getId()));
    assertEquals("NOTIFICATION", ex.resourceType());
    assertEquals(notification.getId(), ex.resourceId());
    org.junit.jupiter.api.Assertions.assertNull(notification.getReadAt());
  }

  private AppUser user() {
    return new AppUser("Marina", "marina@example.test", "hash", Role.CLIENT,
        new Client("Aurora", "Marina", "aurora@example.test", null));
  }

  private Ticket ticket(AppUser user) {
    return new Ticket("SIGE-2001", user.getClient(), null, "Campanha", new DemandType("Ajuste", false, false, "[]"),
        "Google Ads", "Assunto", "Descrição", Priority.MEDIUM, LocalDate.now(), user, user, "{}");
  }
}
