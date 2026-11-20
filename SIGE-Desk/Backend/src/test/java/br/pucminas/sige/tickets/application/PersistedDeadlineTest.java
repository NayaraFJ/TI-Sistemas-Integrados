package br.pucminas.sige.tickets.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import br.pucminas.sige.tickets.domain.Ticket;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PersistedDeadlineTest {
  @Test void flushesBeforeReadingTheDatabaseValue() {
    TicketService service = new TicketService(null,null,null,null,null,null,null,null,null,null,null,null,null,null);
    EntityManager entities = mock(EntityManager.class);
    Ticket ticket = mock(Ticket.class);
    Instant stored = Instant.parse("2026-11-23T18:04:20.606722Z");
    ReflectionTestUtils.setField(service, "entities", entities);
    doAnswer(call -> { when(ticket.getResolutionDueAt()).thenReturn(stored); return null; }).when(entities).refresh(ticket);

    ReflectionTestUtils.invokeMethod(service, "reloadPersistedDeadlines", ticket);

    var order = inOrder(entities);
    order.verify(entities).flush();
    order.verify(entities).refresh(ticket);
    assertEquals(stored, ticket.getResolutionDueAt());
  }
}
