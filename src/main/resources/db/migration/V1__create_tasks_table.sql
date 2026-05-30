CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title varchar(255) NOT NULL,
    description TEXT,
    status varchar(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE tasks IS 'Table of tasks';
COMMENT ON COLUMN tasks.status IS 'Task status PENDING, IN_PROGRESS, COMPLETED';