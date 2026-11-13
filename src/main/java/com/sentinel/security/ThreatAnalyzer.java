package com.sentinel.security;

import com.sentinel.model.SecurityEvent;

import java.util.ArrayList;
import java.util.List;

public class ThreatAnalyzer {
    private final List<DetectionRule> rules = new ArrayList<>();

    public ThreatAnalyzer() {
        rules.add(new BruteForceDetector());
        rules.add(new SuspiciousConnectionDetector());
    }

    public List<SecurityEvent> analyze(List<SecurityEvent> events) {
        List<SecurityEvent> findings = new ArrayList<>();
        for (SecurityEvent event : events) {
            for (DetectionRule rule : rules) {
                if (rule.matches(event)) {
                    findings.add(event);
                    break;
                }
            }
        }
        return findings;
    }
}
