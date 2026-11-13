package com.sentinel.repository;

import com.sentinel.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardRepository {
    public int countSecurityEvents() throws SQLException {
        return count("SELECT COUNT(*) FROM security_events");
    }

    public int countOpenThreats() throws SQLException {
        return count("SELECT COUNT(*) FROM threats WHERE lower(status) = 'open'");
    }

    public int countCriticalActiveAlerts() throws SQLException {
        return count("""
                SELECT COUNT(*) FROM alerts
                WHERE lower(status) = 'active' AND lower(severity) = 'critical'
                """);
    }

    public int countBlockedEvents() throws SQLException {
        return count("SELECT COUNT(*) FROM security_events WHERE lower(investigation_status) = 'blocked'");
    }

    public int countNetworkConnections() throws SQLException {
        return count("SELECT COUNT(*) FROM network_connections");
    }

    public int countOpenIncidents() throws SQLException {
        return count("SELECT COUNT(*) FROM incidents WHERE lower(status) = 'open'");
    }

    public Map<String, Integer> countEventsBySeverity() throws SQLException {
        String sql = "SELECT severity, COUNT(*) AS event_count FROM security_events GROUP BY lower(severity)";
        Map<String, Integer> counts = new LinkedHashMap<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                counts.put(formatSeverity(results.getString("severity")), results.getInt("event_count"));
            }
        }
        return counts;
    }

    public boolean isDatabaseConnected() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery("SELECT 1")) {
            return results.next();
        }
    }

    private int count(String sql) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {
            if (!results.next()) {
                throw new SQLException("The dashboard count query returned no result.");
            }
            return results.getInt(1);
        }
    }

    private String formatSeverity(String severity) {
        if (severity == null || severity.isBlank()) {
            return "Other";
        }
        String normalized = severity.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }
}
