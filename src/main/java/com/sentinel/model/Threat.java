package com.sentinel.model;

import java.time.LocalDateTime;

public record Threat(
        String id,
        String title,
        String description,
        String severity,
        String sourceIp,
        LocalDateTime detectedAt,
        String status
) {
}
