package com.sentinel.model;

import java.time.LocalDateTime;

public record SecurityAlert(
        String id,
        String title,
        String message,
        String severity,
        String status,
        LocalDateTime createdAt,
        LocalDateTime acknowledgedAt
) {
}
