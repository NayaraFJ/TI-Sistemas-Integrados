package br.pucminas.sige.shared.security;

import br.pucminas.sige.users.domain.AppUser;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Negação genérica com dados internos necessários à auditoria após o rollback. */
public class TicketAccessDeniedException extends ResponseStatusException {
  private final AppUser actor;
  private final UUID ticketId;
  public TicketAccessDeniedException(AppUser actor, UUID ticketId) {
    super(HttpStatus.NOT_FOUND, "Ticket não encontrado");
    this.actor = actor;
    this.ticketId = ticketId;
  }
  public AppUser actor() { return actor; }
  public UUID ticketId() { return ticketId; }
}
