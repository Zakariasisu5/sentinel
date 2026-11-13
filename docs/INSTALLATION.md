# Installation and Setup

## Requirements

- Java 21 JDK
- Maven 3.9+
- A Java IDE such as IntelliJ IDEA or VS Code with Java support

## Steps

1. Clone the project to your local machine.
2. Open a terminal in the project root.
3. Run:

```bash
mvn clean install
```

4. Launch the application with:

```bash
mvn javafx:run
```

## Optional setup script

The PowerShell script in `scripts/setup.ps1` can be used to prepare a local environment and print version information for Java and Maven.
