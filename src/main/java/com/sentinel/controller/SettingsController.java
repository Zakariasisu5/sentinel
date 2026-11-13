package com.sentinel.controller;

import com.sentinel.config.AppConfig;
import com.sentinel.config.DatabaseConfig;
import com.sentinel.service.AppSettingsService;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SettingsController {
    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);
    private final AppSettingsService settingsService = new AppSettingsService();

    @FXML
    private ComboBox<String> thresholdCombo;
    @FXML
    private Label statusLabel;
    @FXML
    private Label versionLabel;
    @FXML
    private Label databasePathLabel;

    @FXML
    public void initialize() {
        thresholdCombo.setItems(FXCollections.observableArrayList("Low", "Medium", "High", "Critical"));
        versionLabel.setText(AppConfig.APP_VERSION);
        databasePathLabel.setText(DatabaseConfig.getDatabasePath());
        try {
            String savedThreshold = settingsService.getAlertThreshold();
            thresholdCombo.getSelectionModel().select(capitalize(savedThreshold));
            statusLabel.setText("Preferences loaded from the local database.");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to load application settings.", e);
            thresholdCombo.getSelectionModel().select("Medium");
            statusLabel.setText("Preferences could not be loaded. Check the local database.");
            setError(true);
        }
    }

    @FXML
    private void saveThreshold() {
        String selection = thresholdCombo.getValue();
        if (selection == null) {
            statusLabel.setText("Select an alert threshold before saving.");
            setError(true);
            return;
        }
        try {
            settingsService.setAlertThreshold(selection.toLowerCase());
            statusLabel.setText("Alert threshold saved.");
            setError(false);
        } catch (SQLException | IllegalArgumentException e) {
            logger.error("Unable to save the alert threshold.", e);
            statusLabel.setText("The preference could not be saved.");
            setError(true);
        }
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "Medium";
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
    }

    private void setError(boolean error) {
        statusLabel.getStyleClass().remove("status-error");
        if (error) {
            statusLabel.getStyleClass().add("status-error");
        }
    }
}
