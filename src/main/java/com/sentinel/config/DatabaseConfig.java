package com.sentinel.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DatabaseConfig {
    private static final String DATABASE_NAME = "sentinel.db";
    private static final String SQLITE_PREFIX = "jdbc:sqlite:";

    private DatabaseConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static Path getDatabaseDirectory() {
        return Path.of(System.getProperty("user.home"), ".sentinel");
    }

    public static String getDatabasePath() {
        return getDatabaseDirectory().resolve(DATABASE_NAME).toString();
    }

    public static String getDatabaseUrl() {
        return SQLITE_PREFIX + getDatabasePath();
    }

    public static void ensureDatabaseDirectoryExists() {
        try {
            Files.createDirectories(getDatabaseDirectory());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create the Sentinel database directory.", e);
        }
    }
}
