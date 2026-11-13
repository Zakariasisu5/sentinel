package com.sentinel.repository;

import com.sentinel.model.Incident;
import com.sentinel.database.DatabaseManager;
import com.sentinel.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncidentRepository {
    public void save(Incident incident) {
        // Persistence logic will be added in a later phase.
    }

    public List<Incident> findOpenIncidents() throws SQLException {
        String sql = """
                SELECT id, title, description, severity, status, assigned_to, created_at, resolved_at
                FROM incidents
                WHERE lower(status) = 'open'
                ORDER BY created_at DESC
                LIMIT 200
                """;
        List<Incident> incidents = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                incidents.add(new Incident(
                        results.getString("id"),
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("severity"),
                        results.getString("status"),
                        results.getString("assigned_to"),
                        DateTimeUtils.parseDateTime(results.getString("created_at")),
                        DateTimeUtils.parseDateTime(results.getString("resolved_at"))
                ));
            }
        }
        return incidents;
    }

    public boolean resolve(String incidentId) throws SQLException {
        String sql = """
                UPDATE incidents
                SET status = 'resolved', resolved_at = datetime('now')
                WHERE id = ? AND lower(status) = 'open'
                """;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, incidentId);
            return statement.executeUpdate() == 1;
        }
    }
}
