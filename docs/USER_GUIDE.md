# User Guide

Sentinel is a local-first security operations console. Use the left sidebar to switch between modules; the active page is highlighted. The window can be resized, minimized, maximized, restored, and closed using the normal desktop controls. Its last windowed size and position are saved when it closes.

## Dashboard

The dashboard summarizes locally stored security events, open threats, active critical alerts, and network connection records. The severity chart and recent activity table use records in the local SQLite database. Empty states mean there are no matching records; Sentinel does not generate sample telemetry.

## Threat Monitor

Review open threat records, search by title, source address, or description, and filter by severity. Refresh reloads the latest local records.

## Security Alerts

Search and filter active alerts. Select an alert and choose **Acknowledge selected** to update its status in the database. Sentinel asks for confirmation before applying the change.

## Network Monitor

Search locally stored connection observations by source, destination, protocol, or status. This build does not capture live network traffic.

## IP Intelligence

Search addresses found in local security-event and threat records. The page shows whether an open local threat record references the address. Geolocation and reputation are not enriched because no external intelligence feed is configured.

## Incident Management

Search and filter open incident records. Select an incident and choose **Resolve selected** to update its status after confirmation.

## Analytics

View event counts grouped by severity, alongside totals for events, open threats, and open incidents. Analytics reflect local database records only; no trends or external data are synthesized.

## Settings

Choose and save the local alert-threshold preference. This preference is stored for future integrations; it does not filter records in this build. The page also displays the application version and database path.

## Data location and integrations

The SQLite database is stored at `%USERPROFILE%\\.sentinel\\sentinel.db` on Windows (or `~/.sentinel/sentinel.db` on macOS and Linux). Live telemetry, external reputation services, geolocation feeds, and authentication are not configured in this build.
