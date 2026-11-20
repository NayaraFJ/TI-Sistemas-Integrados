CREATE TABLE security_access_events (
  id CHAR(36) NOT NULL PRIMARY KEY,
  actor_id CHAR(36) NOT NULL,
  actor_role VARCHAR(32) NOT NULL,
  event_code VARCHAR(64) NOT NULL,
  resource_type VARCHAR(32) NOT NULL,
  resource_id CHAR(36) NOT NULL,
  occurred_at DATETIME(6) NOT NULL,
  KEY ix_security_access_actor_occurred (actor_id, occurred_at),
  KEY ix_security_access_resource (resource_type, resource_id, occurred_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
