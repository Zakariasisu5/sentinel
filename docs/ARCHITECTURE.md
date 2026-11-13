# Architecture

Sentinel is organized around a layered design to keep the project maintainable and easy to expand.

## Presentation layer

This layer contains JavaFX screens and styling. FXML files define the layout and CSS files define the appearance. Controllers bridge the UI to the service layer.

## Controller layer

Controllers respond to user actions such as button presses, form submissions, and table refresh events. They should not contain business rules or SQL statements.

## Service layer

Service classes coordinate application operations, such as loading dashboards, evaluating alerts, and tracking incidents.

## Repository layer

Repositories encapsulate SQLite access and are the only classes responsible for SQL queries and database interaction.

## Model layer

The model layer contains data objects such as `SecurityEvent`, `Threat`, and `Incident`.

## Security layer

Threat detection logic is isolated in dedicated rule classes and analyzers. This keeps detection logic reusable and distinct from the UI.

## Configuration and database layers

The project centralizes startup and configuration values in the `config` package and initialization logic in the `database` package.

## Reasoning

This design keeps responsibilities separate and makes it easier to test logic, replace storage implementations, and extend the UI without entangling unrelated concerns.
