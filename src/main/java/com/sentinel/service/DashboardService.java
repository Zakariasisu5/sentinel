package com.sentinel.service;

import com.sentinel.repository.DashboardRepository;

import java.sql.SQLException;
import java.util.Map;

public class DashboardService {
    private final DashboardRepository repository = new DashboardRepository();

    public int getThreatCount() {
        return countOrFail(repository::countOpenThreats);
    }

    public int getBlockedAttackCount() {
        return countOrFail(repository::countBlockedEvents);
    }

    public int getActiveIncidentCount() {
        return countOrFail(repository::countOpenIncidents);
    }

    public String getSystemHealth() {
        try {
            return repository.isDatabaseConnected() ? "Local database connected" : "Database unavailable";
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to check the Sentinel database connection.", e);
        }
    }

    public int getTotalSecurityEventCount() throws SQLException {
        return repository.countSecurityEvents();
    }

    public int getActiveThreatCount() throws SQLException {
        return repository.countOpenThreats();
    }

    public int getCriticalAlertCount() throws SQLException {
        return repository.countCriticalActiveAlerts();
    }

    public int getNetworkConnectionCount() throws SQLException {
        return repository.countNetworkConnections();
    }

    public int getOpenIncidentCount() throws SQLException {
        return repository.countOpenIncidents();
    }

    public Map<String, Integer> getEventCountsBySeverity() throws SQLException {
        return repository.countEventsBySeverity();
    }

    public boolean isDatabaseConnected() throws SQLException {
        return repository.isDatabaseConnected();
    }

    private int countOrFail(CountQuery query) {
        try {
            return query.count();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to retrieve a Sentinel dashboard count.", e);
        }
    }

    @FunctionalInterface
    private interface CountQuery {
        int count() throws SQLException;
    }
}
