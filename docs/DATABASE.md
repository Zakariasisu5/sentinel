# Database Design

Sentinel uses SQLite as the local persistence layer. The database is initialized automatically on first run and stores core SOC data in a small relational structure.

## Core tables

- `security_events`: generic security activity records
- `threats`: identified suspicious patterns and detections
- `alerts`: active and historical alert entries
- `network_connections`: connection metadata
- `incidents`: case records and status tracking
- `app_settings`: user preferences and configuration

## Notes

- Database access is isolated to repositories.
- Prepared statements are preferred for all SQL execution.
- The schema is intentionally designed to be extensible for future monitoring features.
