package br.pucminas.sige.tickets.domain;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, UUID> {
  java.util.Optional<TicketHistory> findFirstByTicketIdAndActionAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(UUID ticketId, String action, java.time.Instant since);
  List<TicketHistory> findByTicketIdOrderByCreatedAtDesc(UUID ticketId);
}
