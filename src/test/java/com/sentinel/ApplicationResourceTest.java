package com.sentinel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationResourceTest {
    @Test
    void applicationIconIsAvailableOnTheClasspath() {
        assertNotNull(Main.class.getResource("/images/icons/sentinel-icon.png"));
    }
}
