package com.sentinel.model;

import java.time.LocalDateTime;

public record SecurityEvent(
        String id,
        String description,
        String sourceIp,
        LocalDateTime eventTime,
        String severity,
        String detectionRule,
        String investigationStatus
) {
}
