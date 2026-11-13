package com.sentinel.repository;

import com.sentinel.model.SecurityAlert;
import com.sentinel.database.DatabaseManager;
import com.sentinel.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlertRepository {
    public void save(SecurityAlert alert) {
        // Persistence logic will be added in a later phase.
    }

    public List<SecurityAlert> findActiveAlerts() throws SQLException {
        String sql = """
                SELECT id, title, message, severity, status, created_at, acknowledged_at
                FROM alerts
                WHERE lower(status) = 'active'
                ORDER BY created_at DESC
                LIMIT 200
                """;
        List<SecurityAlert> alerts = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                alerts.add(new SecurityAlert(
                        results.getString("id"),
                        results.getString("title"),
                        results.getString("message"),
                        results.getString("severity"),
                        results.getString("status"),
                        DateTimeUtils.parseDateTime(results.getString("created_at")),
                        DateTimeUtils.parseDateTime(results.getString("acknowledged_at"))
                ));
            }
        }
        return alerts;
    }

    public boolean acknowledge(String alertId) throws SQLException {
        String sql = """
                UPDATE alerts
                SET status = 'acknowledged', acknowledged_at = datetime('now')
                WHERE id = ? AND lower(status) = 'active'
                """;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, alertId);
            return statement.executeUpdate() == 1;
        }
    }
}
