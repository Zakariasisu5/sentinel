package com.sentinel.util;

public final class NetworkUtils {
    private NetworkUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String normalizeIp(String ipAddress) {
        if (ipAddress == null) {
            return "";
        }
        return ipAddress.trim();
    }
}
