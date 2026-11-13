package com.sentinel.database;

import com.sentinel.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private static final String URL = DatabaseConfig.getDatabaseUrl();

    private DatabaseManager() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() throws SQLException {
        DatabaseConfig.ensureDatabaseDirectoryExists();
        return DriverManager.getConnection(URL);
    }
}
