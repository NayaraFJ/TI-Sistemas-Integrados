package br.pucminas.sige.dashboard.api;

import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.domain.TicketStatus;
import br.pucminas.sige.tickets.application.TicketAccessPolicy;
import java.time.Instant;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/dashboard")
public class DashboardController {
  private final TicketAccessPolicy access; private final CurrentUser current;
  public DashboardController(TicketAccessPolicy access,CurrentUser current){this.access=access;this.current=current;}
  record StatusCount(String status,long count){} record Recent(String id,String number,String subject,String status,String clientName,String updatedAt){} record Response(long activeCount,long highPriorityCount,long validationCount,long waitingCount,long classifiedCount,long overdueCount,List<StatusCount> statusDistribution,List<Recent> recent){}
  @GetMapping public Response dashboard(){var scope=access.visibleTickets(current.require());Instant now=Instant.now();long active=scope.stream().filter(this::active).count();long high=scope.stream().filter(this::active).filter(ticket->ticket.getPriority()==Priority.HIGH||ticket.getPriority()==Priority.URGENT).count();long validation=scope.stream().filter(ticket->ticket.getStatus()==TicketStatus.VALIDATION).count();long waiting=scope.stream().filter(ticket->ticket.getStatus()==TicketStatus.WAITING_FOR_CLIENT).count();long classified=scope.stream().filter(ticket->ticket.getResolutionDueAt()!=null&&active(ticket)).count();long overdue=scope.stream().filter(ticket->ticket.getResolutionDueAt()!=null&&ticket.getStatus()!=TicketStatus.WAITING_FOR_CLIENT&&active(ticket)&&now.isAfter(ticket.getResolutionDueAt())).count();List<StatusCount> distribution=Arrays.stream(TicketStatus.values()).map(status->new StatusCount(status.name(),scope.stream().filter(ticket->ticket.getStatus()==status).count())).toList();List<Recent> recent=scope.stream().sorted(Comparator.comparing(ticket->ticket.getUpdatedAt(),Comparator.reverseOrder())).limit(5).map(ticket->new Recent(ticket.getId().toString(),ticket.getNumber(),ticket.getSubject(),ticket.getStatus().name(),ticket.getClient().getName(),ticket.getUpdatedAt().toString())).toList();return new Response(active,high,validation,waiting,classified,overdue,distribution,recent);}
  private boolean active(br.pucminas.sige.tickets.domain.Ticket ticket){return ticket.getStatus()!=TicketStatus.DONE&&ticket.getStatus()!=TicketStatus.CANCELLED;}
}
