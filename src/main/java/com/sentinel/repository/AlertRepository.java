package com.sentinel.repository;

import com.sentinel.model.SecurityAlert;

import java.util.Collections;
import java.util.List;

public class AlertRepository {
    public void save(SecurityAlert alert) {
        // Persistence logic will be added in a later phase.
    }

    public List<SecurityAlert> findActiveAlerts() {
        return Collections.emptyList();
    }
}
