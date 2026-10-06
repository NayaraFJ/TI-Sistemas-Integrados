CREATE TABLE ticket_deadline_notices (
  id CHAR(36) NOT NULL PRIMARY KEY,
  ticket_id CHAR(36) NOT NULL,
  kind VARCHAR(20) NOT NULL,
  cycle INT NOT NULL,
  deadline DATETIME(6) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  CONSTRAINT fk_deadline_notice_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
  CONSTRAINT uk_deadline_notice UNIQUE (ticket_id,kind,cycle,deadline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_ticket_response_deadline ON tickets(status,response_due_at);
CREATE INDEX idx_ticket_resolution_deadline ON tickets(status,resolution_due_at);
CREATE INDEX idx_ticket_client_updated ON tickets(client_id,updated_at);
CREATE INDEX idx_ticket_assignee_updated ON tickets(assignee_id,updated_at);
CREATE INDEX idx_notification_user_read_created ON notifications(user_id,read_at,created_at);
