package com.sentinel.security;

import com.sentinel.model.SecurityEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreatAnalyzerTest {
    @Test
    void identifiesSuspiciousSecurityEvents() {
        SecurityEvent failedLogin = new SecurityEvent(
                "evt-1",
                "Multiple failed login attempts from external host",
                "203.0.113.55",
                LocalDateTime.now(),
                "high",
                "brute_force_pattern",
                "investigating"
        );

        SecurityEvent normalTraffic = new SecurityEvent(
                "evt-2",
                "Normal browsing activity",
                "10.0.0.8",
                LocalDateTime.now(),
                "low",
                "baseline_monitoring",
                "cleared"
        );

        ThreatAnalyzer analyzer = new ThreatAnalyzer();
        List<SecurityEvent> findings = analyzer.analyze(List.of(failedLogin, normalTraffic));

        assertEquals(1, findings.size());
        assertEquals("evt-1", findings.getFirst().id());
    }
}
