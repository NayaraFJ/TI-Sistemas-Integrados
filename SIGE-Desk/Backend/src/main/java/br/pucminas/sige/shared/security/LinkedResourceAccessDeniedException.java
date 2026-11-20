package br.pucminas.sige.shared.security;

import br.pucminas.sige.users.domain.AppUser;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Negação de recurso existente, auditada após a transação externa. */
public class LinkedResourceAccessDeniedException extends ResponseStatusException {
  private final AppUser actor;
  private final UUID resourceId;
  private final String resourceType;
  public LinkedResourceAccessDeniedException(AppUser actor, UUID resourceId, String resourceType, String message) {
    super(HttpStatus.NOT_FOUND, message);
    this.actor = actor; this.resourceId = resourceId; this.resourceType = resourceType;
  }
  public AppUser actor() { return actor; }
  public UUID resourceId() { return resourceId; }
  public String resourceType() { return resourceType; }
}
