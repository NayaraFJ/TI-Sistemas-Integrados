package br.pucminas.sige.notifications.api;

import br.pucminas.sige.notifications.domain.NotificationRepository;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.application.TicketAccessPolicy;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1/notifications") @Transactional
public class NotificationController {
  private final NotificationRepository notifications; private final CurrentUser current; private final TicketAccessPolicy access;
  public NotificationController(NotificationRepository notifications, CurrentUser current, TicketAccessPolicy access){this.notifications=notifications;this.current=current;this.access=access;}
  record Item(String id,String ticketId,String ticketNumber,String eventType,String summary,Instant readAt,Instant createdAt){}
  record Response(List<Item> items,long unreadCount){}
  @GetMapping public Response list(){var user=current.require();var visible=notifications.findByUserIdOrderByCreatedAtDesc(user.getId()).stream().filter(item->access.canAccess(item.getTicket(),user)).toList();var items=visible.stream().map(item->new Item(item.getId().toString(),item.getTicket().getId().toString(),item.getTicket().getNumber(),item.getEventType(),item.getSummary(),item.getReadAt(),item.getCreatedAt())).toList();return new Response(items,visible.stream().filter(item->item.getReadAt()==null).count());}
  @PostMapping("/{id}/read") @ResponseStatus(HttpStatus.NO_CONTENT) public void markRead(@PathVariable UUID id){var user=current.require();var item=notifications.findById(id).filter(notification->notification.getUser().getId().equals(user.getId())&&access.canAccess(notification.getTicket(),user)).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Notificação não encontrada"));item.markRead();}
  @PostMapping("/read-all") @ResponseStatus(HttpStatus.NO_CONTENT) public void markAllRead(){var user=current.require();notifications.findByUserIdOrderByCreatedAtDesc(user.getId()).stream().filter(item->access.canAccess(item.getTicket(),user)).forEach(notification->notification.markRead());}
}
