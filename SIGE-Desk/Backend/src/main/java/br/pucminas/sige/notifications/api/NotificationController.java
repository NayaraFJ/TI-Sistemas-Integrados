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
  @io.swagger.v3.oas.annotations.media.Schema(name="NotificationItem") record Item(String id,String ticketId,String ticketNumber,String eventType,String summary,@io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) Instant readAt,Instant createdAt){}
  @io.swagger.v3.oas.annotations.media.Schema(name="NotificationResponse") public record Response(List<Item> items,long unreadCount,long total,int page,int size){}
  @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager em;
  @GetMapping public Response list(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="25") int size,@RequestParam(defaultValue="false") boolean unread){
    if(page<0||size<1||size>100||(long)page*size>Integer.MAX_VALUE)throw new IllegalArgumentException("Paginação inválida");var user=current.require();String visible="n.user.id=:userId and "+br.pucminas.sige.shared.security.QueryScope.ticket(user,"n.ticket");String clause=visible+(unread?" and n.readAt is null":"");
    var rows=br.pucminas.sige.shared.security.QueryScope.bind(em.createQuery("select n from Notification n where "+clause+" order by n.createdAt desc,n.id",br.pucminas.sige.notifications.domain.Notification.class).setParameter("userId",user.getId()),user).setFirstResult(page*size).setMaxResults(size).getResultList();
    long total=br.pucminas.sige.shared.security.QueryScope.bind(em.createQuery("select count(n) from Notification n where "+clause,Long.class).setParameter("userId",user.getId()),user).getSingleResult();
    long count=br.pucminas.sige.shared.security.QueryScope.bind(em.createQuery("select count(n) from Notification n where "+visible+" and n.readAt is null",Long.class).setParameter("userId",user.getId()),user).getSingleResult();
    return new Response(rows.stream().map(n->new Item(n.getId().toString(),n.getTicket().getId().toString(),n.getTicket().getNumber(),n.getEventType(),n.getSummary(),n.getReadAt(),n.getCreatedAt())).toList(),count,total,page,size);
  }
  @PostMapping("/{id}/read") @ResponseStatus(HttpStatus.NO_CONTENT) public void markRead(@PathVariable UUID id){var user=current.require();var item=notifications.findById(id).filter(notification->notification.getUser().getId().equals(user.getId())&&access.canAccess(notification.getTicket(),user)).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Notificação não encontrada"));item.markRead();}
  @PostMapping("/read-all") @ResponseStatus(HttpStatus.NO_CONTENT) public void markAllRead(){var user=current.require();String visible="n.user.id=:userId and "+br.pucminas.sige.shared.security.QueryScope.ticket(user,"n.ticket");br.pucminas.sige.shared.security.QueryScope.bind(em.createQuery("update Notification n set n.readAt=:now where n.readAt is null and "+visible).setParameter("now",Instant.now()).setParameter("userId",user.getId()),user).executeUpdate();}
}
