package com.sentinel.config;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public final class WindowStateStore {
    private static final String X = "window.x";
    private static final String Y = "window.y";
    private static final String WIDTH = "window.width";
    private static final String HEIGHT = "window.height";
    private static final String MAXIMIZED = "window.maximized";

    private final Preferences preferences = Preferences.userNodeForPackage(WindowStateStore.class);

    public State load() {
        return new State(
                preferences.getDouble(X, Double.NaN),
                preferences.getDouble(Y, Double.NaN),
                preferences.getDouble(WIDTH, 1440),
                preferences.getDouble(HEIGHT, 900),
                preferences.getBoolean(MAXIMIZED, false)
        );
    }

    public void saveWindowedBounds(double x, double y, double width, double height) {
        preferences.putDouble(X, x);
        preferences.putDouble(Y, y);
        preferences.putDouble(WIDTH, width);
        preferences.putDouble(HEIGHT, height);
    }

    public void saveMaximized(boolean maximized) {
        preferences.putBoolean(MAXIMIZED, maximized);
    }

    public void flush() throws BackingStoreException {
        preferences.flush();
    }

    public record State(double x, double y, double width, double height, boolean maximized) {
    }
}
