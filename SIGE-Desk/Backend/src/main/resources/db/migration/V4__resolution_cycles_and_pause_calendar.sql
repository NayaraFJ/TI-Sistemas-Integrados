ALTER TABLE tickets ADD COLUMN resolution_cycle_started_at TIMESTAMP(6) NULL;
ALTER TABLE tickets ADD COLUMN resolution_pause_intervals JSON NULL;
UPDATE tickets SET resolution_pause_intervals = JSON_ARRAY();
ALTER TABLE tickets MODIFY COLUMN resolution_pause_intervals JSON NOT NULL;
-- Calendários antigos não foram copiados pela versão anterior. Completa apenas os dados ausentes.
UPDATE tickets t JOIN sla_rules r ON r.id = JSON_UNQUOTE(JSON_EXTRACT(t.sla_rule_snapshot, '$.id'))
SET t.sla_rule_snapshot = JSON_SET(t.sla_rule_snapshot,
  '$.businessDays', CAST(r.business_days AS JSON),
  '$.businessStart', CAST(r.business_start AS CHAR),
  '$.businessEnd', CAST(r.business_end AS CHAR),
  '$.holidays', CAST(r.holidays AS JSON))
WHERE t.sla_rule_snapshot IS NOT NULL AND JSON_EXTRACT(t.sla_rule_snapshot, '$.businessDays') IS NULL;
