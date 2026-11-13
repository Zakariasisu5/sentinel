package com.sentinel.controller;

import com.sentinel.model.SecurityEvent;
import com.sentinel.repository.SecurityEventRepository;
import com.sentinel.service.DashboardService;
import com.sentinel.util.DateTimeUtils;
import com.sentinel.util.TableSupport;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final DashboardService dashboardService = new DashboardService();
    private final SecurityEventRepository eventRepository = new SecurityEventRepository();

    @FXML
    private Label totalEventsValue;
    @FXML
    private Label activeThreatsValue;
    @FXML
    private Label criticalAlertsValue;
    @FXML
    private Label networkConnectionsValue;
    @FXML
    private Label connectionStatus;
    @FXML
    private Label chartEmptyState;
    @FXML
    private BarChart<String, Number> severityChart;
    @FXML
    private TableView<SecurityEvent> recentEventsTable;

    @FXML
    public void initialize() {
        TableSupport.configure(recentEventsTable);
        recentEventsTable.getColumns().setAll(List.of(
                TableSupport.stringColumn("Time", event -> DateTimeUtils.formatDateTime(event.eventTime()), 150),
                TableSupport.stringColumn("Severity", SecurityEvent::severity, 95),
                TableSupport.stringColumn("Activity", SecurityEvent::description, 260),
                TableSupport.stringColumn("Source IP", SecurityEvent::sourceIp, 135),
                TableSupport.stringColumn("Status", SecurityEvent::investigationStatus, 110)
        ));
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            totalEventsValue.setText(String.valueOf(dashboardService.getTotalSecurityEventCount()));
            activeThreatsValue.setText(String.valueOf(dashboardService.getActiveThreatCount()));
            criticalAlertsValue.setText(String.valueOf(dashboardService.getCriticalAlertCount()));
            networkConnectionsValue.setText(String.valueOf(dashboardService.getNetworkConnectionCount()));
            connectionStatus.setText(dashboardService.isDatabaseConnected()
                    ? "LOCAL DATABASE CONNECTED"
                    : "DATABASE NOT RESPONDING");

            Map<String, Integer> counts = dashboardService.getEventCountsBySeverity();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            counts.forEach((severity, count) -> series.getData().add(new XYChart.Data<>(severity, count)));
            severityChart.getData().setAll(List.of(series));
            boolean hasEvents = !counts.isEmpty();
            severityChart.setVisible(hasEvents);
            severityChart.setManaged(hasEvents);
            chartEmptyState.setVisible(!hasEvents);
            chartEmptyState.setManaged(!hasEvents);
            recentEventsTable.getItems().setAll(eventRepository.findRecentEvents());
        } catch (SQLException e) {
            logger.error("Unable to load dashboard data.", e);
            showUnavailableState();
        }
    }

    private void showUnavailableState() {
        totalEventsValue.setText("—");
        activeThreatsValue.setText("—");
        criticalAlertsValue.setText("—");
        networkConnectionsValue.setText("—");
        connectionStatus.setText("DATABASE UNAVAILABLE");
        chartEmptyState.setText("Dashboard data could not be loaded.");
        chartEmptyState.setVisible(true);
        chartEmptyState.setManaged(true);
        severityChart.setVisible(false);
        severityChart.setManaged(false);
        severityChart.getData().clear();
        recentEventsTable.getItems().clear();
    }
}
