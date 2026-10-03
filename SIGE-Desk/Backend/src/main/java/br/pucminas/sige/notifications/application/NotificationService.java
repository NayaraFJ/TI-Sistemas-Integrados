package br.pucminas.sige.notifications.application;

import br.pucminas.sige.notifications.domain.Notification;
import br.pucminas.sige.notifications.domain.NotificationRepository;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import br.pucminas.sige.shared.outbox.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
  private final NotificationRepository notifications; private final AppUserRepository users; private final OutboxEventRepository outbox;
  public NotificationService(NotificationRepository notifications, AppUserRepository users, OutboxEventRepository outbox) { this.notifications=notifications; this.users=users; this.outbox=outbox; }
  public void publish(Ticket ticket, String eventType, String summary) {
    Set<UUID> recipients=new HashSet<>(); recipients.add(ticket.getRequester().getId()); if(ticket.getAssignee()!=null) recipients.add(ticket.getAssignee().getId());
    users.findByRoleInAndActiveTrue(List.of(Role.SERVICE,Role.ADMIN)).forEach(user -> recipients.add(user.getId()));
    recipients.forEach(id -> users.findById(id).ifPresent(user -> notifications.save(new Notification(user,ticket,eventType,summary))));
    outbox.save(new OutboxEvent("TICKET",ticket.getId(),eventType,"{\"ticketId\":\""+ticket.getId()+"\",\"eventType\":\""+eventType+"\"}"));
  }
}
