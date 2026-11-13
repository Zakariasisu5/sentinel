# Sentinel – Cybersecurity Command Center

Sentinel is a JavaFX-based desktop application intended to provide a centralized view of cybersecurity activity. It presents a premium SOC-style dashboard for monitoring security events, managing alerts, examining network traffic, and investigating incidents from a single interface.

## Objectives

- Provide a professional, dark-themed cybersecurity dashboard.
- Separate UI, controller, service, repository, security, and database concerns.
- Demonstrate clean Java architecture with JavaFX and Maven.
- Build a foundation suitable for future expansion into real monitoring and alerting workflows.

## Features

- Responsive SOC-style dashboard with local event, threat, alert, and connection summaries.
- Searchable threat, alert, network, IP-observation, and incident views.
- Confirmed alert acknowledgment and incident-resolution actions.
- Event severity analytics and locally persisted alert-threshold preference.
- Resizable window with saved windowed bounds and maximized state.
- SQLite initialization and repository-backed data access.
- Clear empty states; no generated sample telemetry or external-feed claims.

This build does not capture live network traffic or connect to external reputation, geolocation, or threat-intelligence feeds.

## Technology Stack

- Java 21
- JavaFX
- Maven
- SQLite + JDBC
- JavaFX Charts
- ControlsFX
- JUnit 5
- SLF4J + Logback

## Architecture Overview

The project follows a layered design:

- Presentation: JavaFX FXML screens and controllers
- Service layer: application logic and orchestration
- Repository layer: database persistence access
- Model layer: domain objects
- Security layer: detection rules and analyzers
- Configuration and database layers: startup and initialization

## Project Structure

```text
Sentinel/
├── .github/workflows/
├── database/
├── docs/
├── scripts/
├── src/
│   ├── main/
│   │   ├── java/com/sentinel/
│   │   └── resources/
│   └── test/java/com/sentinel/
├── .gitignore
├── CHANGELOG.md
├── CONTRIBUTING.md
├── LICENSE
├── pom.xml
├── README.md
└── ...
```

## Installation

1. Install Java 21 JDK.
2. Install Maven 3.9 or later.
3. Clone or unpack this project to a local folder.
4. Run the following command:

```bash
mvn clean install
```

## How to Run

```bash
mvn javafx:run
```

## Database

The application uses SQLite and initializes the schema automatically on first launch. The schema is defined in `database/schema.sql`, and database access is isolated to the repository and database packages.

The database is stored at `%USERPROFILE%\\.sentinel\\sentinel.db` on Windows and `~/.sentinel/sentinel.db` on macOS and Linux.

## Screenshots

Add screenshots here after running the UI locally and saving a few captures.

## Testing

```bash
mvn test
```

## Future Improvements

- Real network telemetry sources and secure data collection integration.
- Advanced alert correlation and enrichment workflows.
- Threat intelligence feeds and geolocation services.
- Incident timeline management and reporting.
- User authentication and role-based views.

## License

This project is released under the Apache License 2.0. See `LICENSE` for details.
