package br.pucminas.sige.tickets.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketRepository;
import br.pucminas.sige.users.domain.AppUser;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

class TicketVersionTest {
  @Test void matchingVersionFlushesBeforeTheActionAndStaleVersionDoesNotWrite() {
    TicketRepository tickets=mock(TicketRepository.class);
    CurrentUser current=mock(CurrentUser.class);
    TicketAccessPolicy access=mock(TicketAccessPolicy.class);
    Ticket ticket=mock(Ticket.class);
    AppUser actor=mock(AppUser.class);
    UUID id=UUID.randomUUID();
    when(current.require()).thenReturn(actor);
    when(access.requireVisible(id,actor)).thenReturn(ticket);
    when(ticket.getVersionNumber()).thenReturn(6L);
    TicketService service=new TicketService(tickets,null,null,null,null,null,null,null,null,null,null,current,access,null);

    service.verifyVersion(id,"6");
    var order=inOrder(ticket,tickets);
    order.verify(ticket).recordActivity();
    order.verify(tickets).flush();
    clearInvocations(ticket,tickets);
    assertThrows(ObjectOptimisticLockingFailureException.class,()->service.verifyVersion(id,"5"));
    verify(ticket,never()).recordActivity();
    verify(tickets,never()).flush();
  }
}
