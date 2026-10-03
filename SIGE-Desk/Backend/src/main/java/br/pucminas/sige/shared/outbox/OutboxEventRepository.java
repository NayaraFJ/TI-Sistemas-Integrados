package br.pucminas.sige.shared.outbox;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent,UUID>{List<OutboxEvent> findTop100ByProcessedAtIsNullOrderByOccurredAtAsc();}
