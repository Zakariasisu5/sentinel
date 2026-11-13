package com.sentinel.service;

import com.sentinel.model.SecurityAlert;
import com.sentinel.repository.AlertRepository;

import java.util.List;
import java.sql.SQLException;

public class AlertService {
    private final AlertRepository repository = new AlertRepository();

    public List<SecurityAlert> getAlertHistory() throws SQLException {
        return repository.findActiveAlerts();
    }

    public boolean acknowledgeAlert(String alertId) throws SQLException {
        return repository.acknowledge(alertId);
    }
}
