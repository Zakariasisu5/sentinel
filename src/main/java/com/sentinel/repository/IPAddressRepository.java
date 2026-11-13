package com.sentinel.repository;

import com.sentinel.database.DatabaseManager;
import com.sentinel.model.IPAddress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IPAddressRepository {
    public List<IPAddress> findLocallyObservedAddresses(String searchTerm) throws SQLException {
        String sql = """
                SELECT observed.ip_address,
                       CASE WHEN EXISTS (
                           SELECT 1 FROM threats
                           WHERE threats.source_ip = observed.ip_address
                             AND lower(threats.status) = 'open'
                       ) THEN 1 ELSE 0 END AS has_open_threat
                FROM (
                    SELECT source_ip AS ip_address FROM security_events
                    UNION
                    SELECT source_ip AS ip_address FROM threats
                ) observed
                WHERE observed.ip_address IS NOT NULL
                  AND observed.ip_address LIKE ?
                ORDER BY observed.ip_address
                LIMIT 100
                """;
        List<IPAddress> addresses = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + (searchTerm == null ? "" : searchTerm.trim()) + "%");
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    boolean hasOpenThreat = results.getBoolean("has_open_threat");
                    addresses.add(new IPAddress(
                            results.getString("ip_address"),
                            "Not enriched",
                            "Local records only",
                            "No external reputation feed configured",
                            hasOpenThreat
                    ));
                }
            }
        }
        return addresses;
    }
}
