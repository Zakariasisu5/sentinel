package com.sentinel.security;

import com.sentinel.model.SecurityEvent;

public class SuspiciousConnectionDetector implements DetectionRule {
    @Override
    public String getName() {
        return "suspicious_connection_pattern";
    }

    @Override
    public boolean matches(SecurityEvent event) {
        return event.description() != null && event.description().toLowerCase().contains("connection burst");
    }
}
