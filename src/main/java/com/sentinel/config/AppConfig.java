package com.sentinel.config;

public final class AppConfig {
    public static final String APP_TITLE = "Sentinel – Cybersecurity Command Center";
    public static final String APP_VERSION = "0.1.0-SNAPSHOT";
    public static final String DEFAULT_THEME = "dark";
    public static final String DEFAULT_ALERT_THRESHOLD = "medium";

    private AppConfig() {
        throw new IllegalStateException("Utility class");
    }
}
