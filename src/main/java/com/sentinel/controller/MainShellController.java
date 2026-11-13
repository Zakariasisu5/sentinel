package com.sentinel.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainShellController {
    @FXML
    private StackPane contentPane;
    @FXML
    private Label pageHeading;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button threatButton;
    @FXML
    private Button alertsButton;
    @FXML
    private Button networkButton;
    @FXML
    private Button ipButton;
    @FXML
    private Button incidentsButton;
    @FXML
    private Button analyticsButton;
    @FXML
    private Button settingsButton;

    private Button activeButton;

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        loadScreen("/fxml/dashboard.fxml", dashboardButton, "Dashboard");
    }

    @FXML
    public void showThreatMonitor() {
        loadScreen("/fxml/threat-monitor.fxml", threatButton, "Threat Monitor");
    }

    @FXML
    public void showAlerts() {
        loadScreen("/fxml/alerts.fxml", alertsButton, "Security Alerts");
    }

    @FXML
    public void showNetworkMonitor() {
        loadScreen("/fxml/network-monitor.fxml", networkButton, "Network Monitor");
    }

    @FXML
    public void showIpIntelligence() {
        loadScreen("/fxml/ip-intelligence.fxml", ipButton, "IP Intelligence");
    }

    @FXML
    public void showIncidents() {
        loadScreen("/fxml/incidents.fxml", incidentsButton, "Incident Management");
    }

    @FXML
    public void showAnalytics() {
        loadScreen("/fxml/analytics.fxml", analyticsButton, "Analytics");
    }

    @FXML
    public void showSettings() {
        loadScreen("/fxml/settings.fxml", settingsButton, "Settings");
    }

    private void loadScreen(String resourcePath, Button navigationButton, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(resourcePath)));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
            if (activeButton != null) {
                activeButton.getStyleClass().remove("active");
            }
            navigationButton.getStyleClass().add("active");
            activeButton = navigationButton;
            pageHeading.setText(title);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load screen: " + resourcePath, e);
        }
    }
}
