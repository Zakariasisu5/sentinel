package com.sentinel.service;

import com.sentinel.database.DatabaseInitializer;
import com.sentinel.database.DatabaseManager;
import com.sentinel.model.IPAddress;
import com.sentinel.repository.NetworkConnectionRepository;
import com.sentinel.repository.SecurityEventRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseBackedServicesTest {
    @TempDir
    static Path temporaryHome;

    private static String originalUserHome;

    @BeforeAll
    static void initializeDatabase() {
        originalUserHome = System.getProperty("user.home");
        System.setProperty("user.home", temporaryHome.toString());
        DatabaseInitializer.initialize();
    }

    @AfterAll
    static void restoreUserHome() {
        if (originalUserHome == null) {
            System.clearProperty("user.home");
        } else {
            System.setProperty("user.home", originalUserHome);
        }
    }

    @BeforeEach
    void seedLocalRecords() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM security_events");
            statement.executeUpdate("DELETE FROM threats");
            statement.executeUpdate("DELETE FROM alerts");
            statement.executeUpdate("DELETE FROM network_connections");
            statement.executeUpdate("DELETE FROM incidents");
            statement.executeUpdate("""
                    INSERT INTO security_events
                        (id, event_description, source_ip, event_time, severity, detection_rule, investigation_status)
                    VALUES ('evt-1', 'Repeated login failures', '198.51.100.24',
                            '2026-11-13T10:00:00', 'high', 'brute_force', 'investigating')
                    """);
            statement.executeUpdate("""
                    INSERT INTO threats (id, title, description, severity, source_ip, detected_at, status)
                    VALUES ('threat-1', 'Credential attack', 'Repeated login failures',
                            'high', '198.51.100.24', '2026-11-13T10:05:00', 'open')
                    """);
            statement.executeUpdate("""
                    INSERT INTO alerts (id, title, message, severity, status, created_at)
                    VALUES ('alert-1', 'Critical alert', 'Review the activity', 'critical', 'active',
                            '2026-11-13 10:10:00')
                    """);
            statement.executeUpdate("""
                    INSERT INTO network_connections
                        (id, source_ip, destination_ip, protocol, status, bytes_transferred, observed_at)
                    VALUES ('conn-1', '198.51.100.24', '203.0.113.8', 'TCP', 'observed', 2048,
                            '2026-11-13T10:15:00')
                    """);
            statement.executeUpdate("""
                    INSERT INTO incidents (id, title, description, severity, status, created_at)
                    VALUES ('incident-1', 'Credential incident', 'Investigate the source',
                            'high', 'open', '2026-11-13 10:20:00')
                    """);
        }
    }

    @Test
    void dashboardAndRecordViewsReadStoredData() throws SQLException {
        DashboardService dashboard = new DashboardService();

        assertEquals(1, dashboard.getTotalSecurityEventCount());
        assertEquals(1, dashboard.getActiveThreatCount());
        assertEquals(1, dashboard.getCriticalAlertCount());
        assertEquals(1, dashboard.getNetworkConnectionCount());
        assertEquals(Map.of("High", 1), dashboard.getEventCountsBySeverity());
        assertEquals("Repeated login failures",
                new SecurityEventRepository().findRecentEvents().getFirst().description());
        assertEquals(1, new NetworkConnectionRepository().findRecentConnections().size());
    }

    @Test
    void alertAndIncidentActionsUpdateOpenRecords() throws SQLException {
        AlertService alertService = new AlertService();
        IncidentService incidentService = new IncidentService();

        assertTrue(alertService.acknowledgeAlert("alert-1"));
        assertTrue(alertService.getAlertHistory().isEmpty());
        assertTrue(incidentService.resolveIncident("incident-1"));
        assertTrue(incidentService.getIncidentHistory().isEmpty());
    }

    @Test
    void ipLookupReportsOnlyLocalThreatContext() throws SQLException {
        List<IPAddress> addresses = new IPIntelligenceService().investigate("198.51.100");

        assertEquals(1, addresses.size());
        assertEquals("198.51.100.24", addresses.getFirst().ipAddress());
        assertTrue(addresses.getFirst().suspicious());
        assertEquals("No external reputation feed configured", addresses.getFirst().reputation());
    }

    @Test
    void alertThresholdPreferencePersistsInTheLocalDatabase() throws SQLException {
        AppSettingsService settingsService = new AppSettingsService();

        assertEquals("medium", settingsService.getAlertThreshold());
        settingsService.setAlertThreshold("High");
        assertEquals("high", settingsService.getAlertThreshold());
    }
}
