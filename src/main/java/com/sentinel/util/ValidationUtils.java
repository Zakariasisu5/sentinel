package com.sentinel.util;

public final class ValidationUtils {
    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
