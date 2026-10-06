package br.pucminas.sige.tickets.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Ticket> {
  Optional<Ticket> findTopByOrderByCreatedAtDesc();
  List<Ticket> findByClientIdOrderByUpdatedAtDesc(UUID clientId);
  List<Ticket> findByAssigneeIdOrderByUpdatedAtDesc(UUID assigneeId);
  List<Ticket> findAllByOrderByUpdatedAtDesc();
  long countByAssigneeIdAndStatusNotIn(UUID assigneeId, Collection<TicketStatus> statuses);
  long countByClientIdAndStatusNotIn(UUID clientId, Collection<TicketStatus> statuses);
  @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
  @org.springframework.data.jpa.repository.Query("select t from Ticket t where t.id=:id")
  Optional<Ticket> lockForDeadline(UUID id);
  @org.springframework.data.jpa.repository.Query("select t.id from Ticket t where t.status not in ('DONE','CANCELLED') and ((t.responseDueAt<:now and t.responseCompletedAt is null and not exists (select n.id from DeadlineNotice n where n.ticket=t and n.kind='RESPONSE' and n.cycle=0 and n.deadline=t.responseDueAt)) or (t.resolutionDueAt<:now and t.resolutionPausedAt is null and not exists (select n.id from DeadlineNotice n where n.ticket=t and n.kind='RESOLUTION' and n.cycle=t.resolutionCycle and n.deadline=t.resolutionDueAt))) order by t.id")
  List<UUID> findPendingDeadlineNotices(java.time.Instant now,org.springframework.data.domain.Pageable page);
}
