package br.pucminas.sige.tickets.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
  Optional<Ticket> findTopByOrderByCreatedAtDesc();
  List<Ticket> findByClientIdOrderByUpdatedAtDesc(UUID clientId);
  List<Ticket> findByAssigneeIdOrderByUpdatedAtDesc(UUID assigneeId);
  List<Ticket> findAllByOrderByUpdatedAtDesc();
  long countByAssigneeIdAndStatusNotIn(UUID assigneeId, Collection<TicketStatus> statuses);
  long countByClientIdAndStatusNotIn(UUID clientId, Collection<TicketStatus> statuses);
}
