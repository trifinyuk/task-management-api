CREATE INDEX idx_tasks_status ON tasks(status);

COMMENT ON INDEX idx_tasks_status IS 'Index for fast filtering by task status';