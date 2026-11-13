package com.sentinel.repository;

import com.sentinel.model.Threat;
import com.sentinel.database.DatabaseManager;
import com.sentinel.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThreatRepository {
    public void save(Threat threat) {
        // Persistence logic will be added in a later phase.
    }

    public List<Threat> findOpenThreats() throws SQLException {
        String sql = """
                SELECT id, title, description, severity, source_ip, detected_at, status
                FROM threats
                WHERE lower(status) = 'open'
                ORDER BY detected_at DESC
                LIMIT 200
                """;
        List<Threat> threats = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                threats.add(new Threat(
                        results.getString("id"),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("severity"),
                        results.getString("source_ip"),
                        DateTimeUtils.parseDateTime(results.getString("detected_at")),
                        results.getString("status")
                ));
            }
        }
        return threats;
    }
}
