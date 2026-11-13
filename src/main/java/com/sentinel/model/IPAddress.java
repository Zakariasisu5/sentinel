package com.sentinel.model;

public record IPAddress(
        String ipAddress,
        String country,
        String region,
        String reputation,
        boolean suspicious
) {
}
