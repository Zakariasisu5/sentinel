package com.sentinel.model;

import java.time.LocalDateTime;

public record Incident(
        String id,
        String title,
        String description,
        String severity,
        String status,
        String assignedTo,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {
}
