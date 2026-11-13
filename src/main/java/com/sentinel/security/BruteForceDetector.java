package com.sentinel.security;

import com.sentinel.model.SecurityEvent;

public class BruteForceDetector implements DetectionRule {
    @Override
    public String getName() {
        return "brute_force_pattern";
    }

    @Override
    public boolean matches(SecurityEvent event) {
        return event.description() != null && event.description().toLowerCase().contains("failed login");
    }
}
