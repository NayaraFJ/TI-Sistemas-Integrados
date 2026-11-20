package br.pucminas.sige.shared.security;

import br.pucminas.sige.users.domain.AppUser;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** Trilha de segurança independente do histórico de negócio do ticket. */
@Service
public class SecurityAccessAudit {
  private static final Logger log = LoggerFactory.getLogger(SecurityAccessAudit.class);
  private final JdbcTemplate jdbc;

  public SecurityAccessAudit(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  // A resposta negada provoca rollback no fluxo externo; a trilha deve sobreviver a ele.
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void ticketAccessDenied(AppUser actor, UUID ticketId) {
    jdbc.update("INSERT INTO security_access_events (id, actor_id, actor_role, event_code, resource_type, resource_id, occurred_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
      UUID.randomUUID().toString(), actor.getId().toString(), actor.getRole().name(),
      "SECURITY_TICKET_ACCESS_DENIED", "TICKET", ticketId.toString(), Timestamp.from(Instant.now()));
    log.warn("eventCode=SECURITY_TICKET_ACCESS_DENIED actorId={} role={} resourceType=TICKET resourceId={}",
      actor.getId(), actor.getRole(), ticketId);
  }
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void linkedResourceAccessDenied(AppUser actor, UUID resourceId, String resourceType) {
    String code = "SECURITY_" + resourceType + "_ACCESS_DENIED";
    jdbc.update("INSERT INTO security_access_events (id, actor_id, actor_role, event_code, resource_type, resource_id, occurred_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
      UUID.randomUUID().toString(), actor.getId().toString(), actor.getRole().name(), code, resourceType, resourceId.toString(), Timestamp.from(Instant.now()));
    log.warn("eventCode={} actorId={} role={} resourceType={} resourceId={}", code, actor.getId(), actor.getRole(), resourceType, resourceId);
  }
}
