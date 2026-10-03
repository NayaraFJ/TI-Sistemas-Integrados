package br.pucminas.sige.sla.domain;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaRuleRepository extends JpaRepository<SlaRule, UUID> {
  List<SlaRule> findByActiveTrue();
}
