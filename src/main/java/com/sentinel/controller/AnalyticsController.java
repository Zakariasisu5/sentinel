package com.sentinel.controller;

import com.sentinel.service.DashboardService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AnalyticsController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);
    private final DashboardService dashboardService = new DashboardService();

    @FXML
    private Label totalEventsValue;
    @FXML
    private Label activeThreatsValue;
    @FXML
    private Label activeIncidentsValue;
    @FXML
    private Label statusLabel;
    @FXML
    private Label emptyState;
    @FXML
    private BarChart<String, Number> severityChart;

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            totalEventsValue.setText(String.valueOf(dashboardService.getTotalSecurityEventCount()));
            activeThreatsValue.setText(String.valueOf(dashboardService.getActiveThreatCount()));
            activeIncidentsValue.setText(String.valueOf(dashboardService.getOpenIncidentCount()));
            Map<String, Integer> counts = dashboardService.getEventCountsBySeverity();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            counts.forEach((severity, count) -> series.getData().add(new XYChart.Data<>(severity, count)));
            severityChart.getData().setAll(List.of(series));
            boolean hasEvents = !counts.isEmpty();
            severityChart.setVisible(hasEvents);
            severityChart.setManaged(hasEvents);
            emptyState.setVisible(!hasEvents);
            emptyState.setManaged(!hasEvents);
            statusLabel.setText(hasEvents
                    ? "Showing " + counts.values().stream().mapToInt(Integer::intValue).sum() + " recorded event(s)."
                    : "No local event records are available.");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to load analytics.", e);
            totalEventsValue.setText("—");
            activeThreatsValue.setText("—");
            activeIncidentsValue.setText("—");
            severityChart.getData().clear();
            severityChart.setVisible(false);
            severityChart.setManaged(false);
            emptyState.setText("Analytics could not be loaded.");
            emptyState.setVisible(true);
            emptyState.setManaged(true);
            statusLabel.setText("Check the local database and refresh.");
            setError(true);
        }
    }

    private void setError(boolean error) {
        statusLabel.getStyleClass().remove("status-error");
        if (error) {
            statusLabel.getStyleClass().add("status-error");
        }
    }
}
