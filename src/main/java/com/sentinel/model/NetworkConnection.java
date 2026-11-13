package com.sentinel.model;

import java.time.LocalDateTime;

public record NetworkConnection(
        String id,
        String sourceIp,
        String destinationIp,
        String protocol,
        String status,
        long bytesTransferred,
        LocalDateTime observedAt
) {
}
