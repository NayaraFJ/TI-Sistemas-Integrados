package br.pucminas.sige.files.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.pucminas.sige.files.domain.TicketAttachment;
import br.pucminas.sige.files.domain.TicketAttachmentRepository;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.shared.security.LinkedResourceAccessDeniedException;
import br.pucminas.sige.tickets.application.TicketAccessPolicy;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class TicketFileServiceTest {
  @Test
  void auditsOnlyCrossedAttachmentsOutsideTheActorsScope() {
    var repository = mock(TicketAttachmentRepository.class);
    var access = mock(TicketAccessPolicy.class);
    var current = mock(CurrentUser.class);
    var actor = mock(AppUser.class);
    var requested = mock(Ticket.class);
    var actual = mock(Ticket.class);
    var attachment = mock(TicketAttachment.class);
    var requestedId = UUID.randomUUID();
    var attachmentId = UUID.randomUUID();
    when(current.require()).thenReturn(actor);
    when(access.requireVisible(requestedId, actor)).thenReturn(requested);
    when(requested.getId()).thenReturn(requestedId);
    when(actual.getId()).thenReturn(UUID.randomUUID());
    when(attachment.getTicket()).thenReturn(actual);
    when(repository.findById(attachmentId)).thenReturn(Optional.of(attachment));
    var service = new TicketFileService(repository, access, current, ".", null, null);

    var denied = assertThrows(LinkedResourceAccessDeniedException.class, () -> service.download(requestedId, attachmentId));
    assertEquals("ATTACHMENT", denied.resourceType());
    assertEquals(attachmentId, denied.resourceId());
    when(access.canAccess(actual, actor)).thenReturn(true);
    var mismatch = assertThrows(ResponseStatusException.class, () -> service.download(requestedId, attachmentId));
    assertEquals(ResponseStatusException.class, mismatch.getClass());
    assertEquals(404, mismatch.getStatusCode().value());
    when(repository.findById(attachmentId)).thenReturn(Optional.empty());
    var missing = assertThrows(ResponseStatusException.class, () -> service.download(requestedId, attachmentId));
    assertEquals(ResponseStatusException.class, missing.getClass());
    assertEquals(404, missing.getStatusCode().value());
  }
}
