package br.pucminas.sige.notifications.application;
import br.pucminas.sige.notifications.domain.*;
import br.pucminas.sige.tickets.domain.*;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
class DeadlineNotificationServiceTest {
  private final TicketRepository tickets=mock(TicketRepository.class);
  private final DeadlineNoticeRepository notices=mock(DeadlineNoticeRepository.class);
  private final NotificationService notifications=mock(NotificationService.class);
  private final TicketHistoryRepository history=mock(TicketHistoryRepository.class);
  private final DeadlineNotificationService service=new DeadlineNotificationService(tickets,notices,notifications,history);
  private final Instant due=Instant.parse("2026-10-01T12:00:00Z"),now=due.plusSeconds(60);
  private Ticket ticket(){Ticket t=mock(Ticket.class);when(t.getId()).thenReturn(UUID.randomUUID());when(t.getNumber()).thenReturn("SIGE-test");when(t.getStatus()).thenReturn(TicketStatus.EXECUTION);when(t.getResolutionCycle()).thenReturn(1);when(tickets.lockForDeadline(t.getId())).thenReturn(Optional.of(t));return t;}
  @Test void notifiesResponseEvenWhenResolutionIsPaused(){Ticket t=ticket();when(t.getResponseDueAt()).thenReturn(due);when(t.getResolutionDueAt()).thenReturn(due);when(t.getResolutionPausedAt()).thenReturn(due.minusSeconds(60));service.inspect(t.getId(),now);verify(notices).saveAndFlush(any());verify(notifications).publish(eq(t),eq("SLA_OVERDUE"),contains("primeira resposta"));verify(notifications,never()).publish(eq(t),eq("SLA_OVERDUE"),contains("resolução"));}
  @Test void deduplicatesTheSameDeadline(){Ticket t=ticket();when(t.getResolutionDueAt()).thenReturn(due);when(notices.existsByTicketIdAndKindAndCycleAndDeadline(t.getId(),"RESOLUTION",1,due)).thenReturn(true);service.inspect(t.getId(),now);verifyNoInteractions(notifications);verify(notices,never()).saveAndFlush(any());}
  @Test void emitsForANewCycle(){Ticket t=ticket();when(t.getResolutionCycle()).thenReturn(2);when(t.getResolutionDueAt()).thenReturn(due);service.inspect(t.getId(),now);verify(notices).existsByTicketIdAndKindAndCycleAndDeadline(t.getId(),"RESOLUTION",2,due);verify(notifications).publish(eq(t),eq("SLA_OVERDUE"),contains("resolução"));}
  @Test void doesNotNotifyCompletedTicketsOrCompletedResponses(){Ticket t=ticket();when(t.getStatus()).thenReturn(TicketStatus.DONE);when(t.getResponseDueAt()).thenReturn(due);service.inspect(t.getId(),now);verifyNoInteractions(notifications);when(t.getStatus()).thenReturn(TicketStatus.EXECUTION);when(t.getResponseCompletedAt()).thenReturn(now);service.inspect(t.getId(),now);verifyNoInteractions(notifications);}
}
