package com.sentinel.service;

import com.sentinel.repository.AppSettingsRepository;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;

public class AppSettingsService {
    private static final String ALERT_THRESHOLD_KEY = "alert_threshold";
    private static final Set<String> ALLOWED_THRESHOLDS = Set.of("low", "medium", "high", "critical");
    private final AppSettingsRepository repository = new AppSettingsRepository();

    public String getAlertThreshold() throws SQLException {
        return repository.findValue(ALERT_THRESHOLD_KEY);
    }

    public void setAlertThreshold(String threshold) throws SQLException {
        String normalized = threshold == null ? null : threshold.toLowerCase(Locale.ROOT);
        if (normalized == null || !ALLOWED_THRESHOLDS.contains(normalized)) {
            throw new IllegalArgumentException("Alert threshold must be low, medium, high, or critical.");
        }
        repository.saveValue(ALERT_THRESHOLD_KEY, normalized);
    }
}
