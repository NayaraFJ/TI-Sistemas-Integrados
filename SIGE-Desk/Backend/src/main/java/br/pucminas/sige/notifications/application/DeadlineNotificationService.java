package br.pucminas.sige.notifications.application;
import br.pucminas.sige.notifications.domain.*;
import br.pucminas.sige.tickets.domain.*;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class DeadlineNotificationService {
  private final TicketRepository tickets;private final DeadlineNoticeRepository notices;private final NotificationService notifications;private final TicketHistoryRepository history;
  public DeadlineNotificationService(TicketRepository tickets,DeadlineNoticeRepository notices,NotificationService notifications,TicketHistoryRepository history){this.tickets=tickets;this.notices=notices;this.notifications=notifications;this.history=history;}
  @Transactional public void inspect(UUID id,Instant now){
    Ticket ticket=tickets.lockForDeadline(id).orElse(null);
    if(ticket==null||ticket.getStatus()==TicketStatus.DONE||ticket.getStatus()==TicketStatus.CANCELLED)return;
    if(ticket.getResponseCompletedAt()==null&&ticket.getResponseDueAt()!=null&&now.isAfter(ticket.getResponseDueAt()))publish(ticket,"RESPONSE",0,ticket.getResponseDueAt());
    if(ticket.getResolutionPausedAt()==null&&ticket.getResolutionDueAt()!=null&&now.isAfter(ticket.getResolutionDueAt()))publish(ticket,"RESOLUTION",ticket.getResolutionCycle(),ticket.getResolutionDueAt());
  }
  private void publish(Ticket ticket,String kind,int cycle,Instant deadline){
    if(notices.existsByTicketIdAndKindAndCycleAndDeadline(ticket.getId(),kind,cycle,deadline))return;
    notices.saveAndFlush(new DeadlineNotice(ticket,kind,cycle,deadline));
    String label=kind.equals("RESPONSE")?"primeira resposta":"resolução";
    history.save(new TicketHistory(ticket,null,"SLA_OVERDUE",kind.equals("RESPONSE")?"responseDueAt":"resolutionDueAt",null,"\""+deadline+"\"","Prazo de "+label+" vencido"));
    notifications.publish(ticket,"SLA_OVERDUE",ticket.getNumber()+" está com o prazo de "+label+" vencido.");
  }
}
