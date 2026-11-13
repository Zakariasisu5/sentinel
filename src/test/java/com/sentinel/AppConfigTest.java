package com.sentinel;

import com.sentinel.config.AppConfig;
import com.sentinel.config.DatabaseConfig;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppConfigTest {
    @Test
    void appMetadataIsConfigured() {
        assertEquals("Sentinel – Cybersecurity Command Center", AppConfig.APP_TITLE);
        assertEquals("0.1.0-SNAPSHOT", AppConfig.APP_VERSION);
        assertEquals("dark", AppConfig.DEFAULT_THEME);
    }

    @Test
    void databaseDirectoryIsCreated() throws Exception {
        Path expectedDirectory = DatabaseConfig.getDatabaseDirectory();

        DatabaseConfig.ensureDatabaseDirectoryExists();

        assertTrue(Files.isDirectory(expectedDirectory));
    }
}
