CREATE TABLE clients (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(160) NOT NULL,
  contact_name VARCHAR(160) NOT NULL,
  email VARCHAR(254) NOT NULL,
  phone VARCHAR(40) NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  UNIQUE KEY uk_clients_email (email),
  KEY ix_clients_active_name (active, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE users (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(160) NOT NULL,
  email VARCHAR(254) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL,
  client_id CHAR(36) NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_users_client FOREIGN KEY (client_id) REFERENCES clients (id),
  CONSTRAINT ck_users_role CHECK (role IN ('CLIENT','SERVICE','TRAFFIC_MANAGER','ADMIN')),
  UNIQUE KEY uk_users_email (email),
  KEY ix_users_client_active (client_id, active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE campaigns (
  id CHAR(36) NOT NULL PRIMARY KEY,
  client_id CHAR(36) NOT NULL,
  name VARCHAR(160) NOT NULL,
  channel VARCHAR(80) NOT NULL,
  objective VARCHAR(160) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_campaigns_client FOREIGN KEY (client_id) REFERENCES clients (id),
  KEY ix_campaigns_client_active (client_id, active, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE demand_types (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(160) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  approval_required BOOLEAN NOT NULL,
  evidence_required BOOLEAN NOT NULL,
  field_definitions JSON NOT NULL,
  version_number INT NOT NULL DEFAULT 1,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  UNIQUE KEY uk_demand_types_name (name),
  KEY ix_demand_types_active_name (active, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sla_rules (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(160) NOT NULL,
  scope VARCHAR(32) NOT NULL,
  client_id CHAR(36) NULL,
  demand_type_id CHAR(36) NULL,
  timezone VARCHAR(64) NOT NULL,
  business_days JSON NOT NULL,
  business_start TIME NOT NULL,
  business_end TIME NOT NULL,
  holidays JSON NOT NULL,
  pause_in_validation BOOLEAN NOT NULL DEFAULT FALSE,
  deadlines JSON NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  version_number INT NOT NULL DEFAULT 1,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_sla_rules_client FOREIGN KEY (client_id) REFERENCES clients (id),
  CONSTRAINT fk_sla_rules_type FOREIGN KEY (demand_type_id) REFERENCES demand_types (id),
  CONSTRAINT ck_sla_scope CHECK (scope IN ('DEFAULT','CLIENT','DEMAND_TYPE','CLIENT_AND_DEMAND_TYPE')),
  KEY ix_sla_rules_scope_active (scope, active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tickets (
  id CHAR(36) NOT NULL PRIMARY KEY,
  number VARCHAR(32) NOT NULL,
  client_id CHAR(36) NOT NULL,
  campaign_id CHAR(36) NULL,
  pending_campaign VARCHAR(255) NULL,
  demand_type_id CHAR(36) NOT NULL,
  type_snapshot JSON NOT NULL,
  channel VARCHAR(80) NOT NULL,
  subject VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  urgency VARCHAR(16) NOT NULL,
  priority VARCHAR(16) NULL,
  desired_date DATE NULL,
  requester_id CHAR(36) NOT NULL,
  author_id CHAR(36) NOT NULL,
  assignee_id CHAR(36) NULL,
  status VARCHAR(32) NOT NULL,
  wait_origin VARCHAR(16) NULL,
  wait_reason TEXT NULL,
  complement_received BOOLEAN NOT NULL DEFAULT FALSE,
  execution_started BOOLEAN NOT NULL DEFAULT FALSE,
  resolution_cycle INT NOT NULL DEFAULT 1,
  response_due_at DATETIME(6) NULL,
  resolution_due_at DATETIME(6) NULL,
  response_completed_at DATETIME(6) NULL,
  resolution_paused_at DATETIME(6) NULL,
  sla_state VARCHAR(32) NOT NULL,
  sla_rule_snapshot JSON NULL,
  metrics JSON NULL,
  version_number BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_tickets_client FOREIGN KEY (client_id) REFERENCES clients (id),
  CONSTRAINT fk_tickets_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns (id),
  CONSTRAINT fk_tickets_type FOREIGN KEY (demand_type_id) REFERENCES demand_types (id),
  CONSTRAINT fk_tickets_requester FOREIGN KEY (requester_id) REFERENCES users (id),
  CONSTRAINT fk_tickets_author FOREIGN KEY (author_id) REFERENCES users (id),
  CONSTRAINT fk_tickets_assignee FOREIGN KEY (assignee_id) REFERENCES users (id),
  CONSTRAINT ck_tickets_status CHECK (status IN ('OPEN','TRIAGE','EXECUTION','WAITING_FOR_CLIENT','VALIDATION','DONE','REOPENED','CANCELLED')),
  CONSTRAINT ck_tickets_urgency CHECK (urgency IN ('URGENT','HIGH','MEDIUM','LOW')),
  CONSTRAINT ck_tickets_priority CHECK (priority IS NULL OR priority IN ('URGENT','HIGH','MEDIUM','LOW')),
  UNIQUE KEY uk_tickets_number (number),
  KEY ix_tickets_client_status (client_id, status, updated_at),
  KEY ix_tickets_assignee_status (assignee_id, status, updated_at),
  KEY ix_tickets_status_updated (status, updated_at),
  KEY ix_tickets_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ticket_sequences (
  name VARCHAR(32) NOT NULL PRIMARY KEY,
  next_value BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO ticket_sequences (name, next_value) VALUES ('SIGE', 1000);

CREATE TABLE ticket_comments (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ticket_id CHAR(36) NOT NULL,
  author_id CHAR(36) NOT NULL,
  body TEXT NOT NULL,
  effective_response BOOLEAN NOT NULL DEFAULT FALSE,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_ticket_comments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
  CONSTRAINT fk_ticket_comments_author FOREIGN KEY (author_id) REFERENCES users (id),
  KEY ix_ticket_comments_ticket_created (ticket_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ticket_attachments (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ticket_id CHAR(36) NOT NULL,
  author_id CHAR(36) NOT NULL,
  original_name VARCHAR(255) NOT NULL,
  storage_key VARCHAR(255) NOT NULL,
  content_type VARCHAR(120) NOT NULL,
  size_bytes BIGINT NOT NULL,
  kind VARCHAR(16) NOT NULL,
  evidence_description VARCHAR(500) NULL,
  evidence_url VARCHAR(2048) NULL,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_ticket_attachments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
  CONSTRAINT fk_ticket_attachments_author FOREIGN KEY (author_id) REFERENCES users (id),
  CONSTRAINT ck_ticket_attachments_kind CHECK (kind IN ('ATTACHMENT','EVIDENCE')),
  KEY ix_ticket_attachments_ticket_created (ticket_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ticket_approvals (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ticket_id CHAR(36) NOT NULL,
  decision VARCHAR(32) NOT NULL,
  decided_by_id CHAR(36) NULL,
  note TEXT NULL,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_ticket_approvals_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
  CONSTRAINT fk_ticket_approvals_user FOREIGN KEY (decided_by_id) REFERENCES users (id),
  KEY ix_ticket_approvals_ticket_created (ticket_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ticket_history (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ticket_id CHAR(36) NOT NULL,
  actor_id CHAR(36) NULL,
  action VARCHAR(120) NOT NULL,
  field_name VARCHAR(100) NULL,
  old_value JSON NULL,
  new_value JSON NULL,
  reason TEXT NULL,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_ticket_history_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
  CONSTRAINT fk_ticket_history_actor FOREIGN KEY (actor_id) REFERENCES users (id),
  KEY ix_ticket_history_ticket_created (ticket_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE notifications (
  id CHAR(36) NOT NULL PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  ticket_id CHAR(36) NOT NULL,
  event_type VARCHAR(64) NOT NULL,
  summary VARCHAR(500) NOT NULL,
  read_at DATETIME(6) NULL,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_notifications_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
  KEY ix_notifications_user_read_created (user_id, read_at, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE outbox_events (
  id CHAR(36) NOT NULL PRIMARY KEY,
  aggregate_type VARCHAR(80) NOT NULL,
  aggregate_id CHAR(36) NOT NULL,
  event_type VARCHAR(100) NOT NULL,
  payload JSON NOT NULL,
  occurred_at DATETIME(6) NOT NULL,
  processed_at DATETIME(6) NULL,
  KEY ix_outbox_events_pending (processed_at, occurred_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
