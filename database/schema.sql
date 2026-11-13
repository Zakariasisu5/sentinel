CREATE TABLE IF NOT EXISTS security_events (
    id TEXT PRIMARY KEY,
    event_description TEXT NOT NULL,
    source_ip TEXT,
    event_time TEXT NOT NULL,
    severity TEXT NOT NULL,
    detection_rule TEXT,
    investigation_status TEXT NOT NULL DEFAULT 'new'
);

CREATE TABLE IF NOT EXISTS threats (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    severity TEXT NOT NULL,
    source_ip TEXT,
    detected_at TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'open'
);

CREATE TABLE IF NOT EXISTS alerts (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    severity TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'active',
    created_at TEXT NOT NULL,
    acknowledged_at TEXT
);

CREATE TABLE IF NOT EXISTS network_connections (
    id TEXT PRIMARY KEY,
    source_ip TEXT NOT NULL,
    destination_ip TEXT NOT NULL,
    protocol TEXT NOT NULL,
    status TEXT NOT NULL,
    bytes_transferred INTEGER DEFAULT 0,
    observed_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS incidents (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    severity TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'open',
    assigned_to TEXT,
    created_at TEXT NOT NULL,
    resolved_at TEXT
);

CREATE TABLE IF NOT EXISTS app_settings (
    key TEXT PRIMARY KEY,
    value TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

INSERT OR IGNORE INTO app_settings (key, value, updated_at) VALUES
    ('app_name', 'Sentinel - Cybersecurity Command Center', datetime('now')),
    ('theme', 'dark', datetime('now')),
    ('alert_threshold', 'medium', datetime('now'));
