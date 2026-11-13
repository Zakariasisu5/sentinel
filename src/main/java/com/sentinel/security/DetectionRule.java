package com.sentinel.security;

import com.sentinel.model.SecurityEvent;

public interface DetectionRule {
    String getName();

    boolean matches(SecurityEvent event);
}
