ALTER TABLE tickets ADD COLUMN completed_at DATETIME(6) NULL;

UPDATE tickets
SET completed_at = updated_at
WHERE status = 'DONE' AND completed_at IS NULL;
