package com.sentinel.repository;

import com.sentinel.database.DatabaseManager;
import com.sentinel.model.NetworkConnection;
import com.sentinel.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NetworkConnectionRepository {
    public List<NetworkConnection> findRecentConnections() throws SQLException {
        String sql = """
                SELECT id, source_ip, destination_ip, protocol, status, bytes_transferred, observed_at
                FROM network_connections
                ORDER BY observed_at DESC
                LIMIT 200
                """;
        List<NetworkConnection> connections = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                connections.add(new NetworkConnection(
                        results.getString("id"),
                        results.getString("source_ip"),
                        results.getString("destination_ip"),
                        results.getString("protocol"),
                        results.getString("status"),
                        results.getLong("bytes_transferred"),
                        DateTimeUtils.parseDateTime(results.getString("observed_at"))
                ));
            }
        }
        return connections;
    }
}
