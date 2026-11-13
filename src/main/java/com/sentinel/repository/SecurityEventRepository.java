package com.sentinel.repository;

import com.sentinel.model.SecurityEvent;
import com.sentinel.database.DatabaseManager;
import com.sentinel.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecurityEventRepository {
    public void save(SecurityEvent event) {
        // Repository persistence logic will be implemented in later phases.
    }

    public List<SecurityEvent> findRecentEvents() throws SQLException {
        String sql = """
                SELECT id, event_description, source_ip, event_time, severity,
                       detection_rule, investigation_status
                FROM security_events
                ORDER BY event_time DESC
                LIMIT 100
                """;
        List<SecurityEvent> events = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                events.add(new SecurityEvent(
                        results.getString("id"),
                        results.getString("event_description"),
                        results.getString("source_ip"),
                        DateTimeUtils.parseDateTime(results.getString("event_time")),
                        results.getString("severity"),
                        results.getString("detection_rule"),
                        results.getString("investigation_status")
                ));
            }
        }
        return events;
    }
}
