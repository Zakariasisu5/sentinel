package com.sentinel.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public final class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private DatabaseInitializer() {
        throw new IllegalStateException("Utility class");
    }

    public static void initialize() {
        try {
            Path schemaPath = Path.of("database", "schema.sql");
            String schema = Files.readString(schemaPath, StandardCharsets.UTF_8);

            try (Connection connection = DatabaseManager.getConnection();
                 Statement statement = connection.createStatement()) {
                Arrays.stream(schema.split(";"))
                        .map(String::trim)
                        .filter(sql -> !sql.isEmpty())
                        .forEach(sql -> {
                            try {
                                statement.execute(sql);
                            } catch (SQLException e) {
                                throw new IllegalStateException("Failed to execute SQL: " + sql, e);
                            }
                        });
            }

            logger.info("Database initialization completed successfully.");
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("Failed to initialize the Sentinel database.", e);
        }
    }
}
