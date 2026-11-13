package com.sentinel.controller;

import com.sentinel.model.SecurityAlert;
import com.sentinel.service.AlertService;
import com.sentinel.util.DateTimeUtils;
import com.sentinel.util.TableSupport;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final AlertService alertService = new AlertService();
    private final javafx.collections.ObservableList<SecurityAlert> alerts = FXCollections.observableArrayList();
    private FilteredList<SecurityAlert> filteredAlerts;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> severityFilter;
    @FXML
    private Button acknowledgeButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<SecurityAlert> alertsTable;

    @FXML
    public void initialize() {
        TableSupport.configure(alertsTable);
        alertsTable.getColumns().setAll(List.of(
                TableSupport.stringColumn("Created", alert -> DateTimeUtils.formatDateTime(alert.createdAt()), 145),
                TableSupport.stringColumn("Severity", SecurityAlert::severity, 90),
                TableSupport.stringColumn("Alert", SecurityAlert::title, 165),
                TableSupport.stringColumn("Details", SecurityAlert::message, 250),
                TableSupport.stringColumn("Status", SecurityAlert::status, 90)
        ));
        severityFilter.setItems(FXCollections.observableArrayList(
                "All severities", "Critical", "High", "Medium", "Low"
        ));
        severityFilter.getSelectionModel().selectFirst();
        filteredAlerts = new FilteredList<>(alerts);
        alertsTable.setItems(filteredAlerts);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        severityFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        alertsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> acknowledgeButton.setDisable(selected == null));
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            List<SecurityAlert> records = alertService.getAlertHistory();
            alerts.setAll(records);
            statusLabel.setText(records.size() + " active alert(s)");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to load active alerts.", e);
            alerts.clear();
            statusLabel.setText("Alert records could not be loaded. Check the local database.");
            setError(true);
        }
    }

    @FXML
    private void acknowledgeSelected() {
        SecurityAlert selected = alertsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Acknowledge \"" + selected.title() + "\"?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirmation.setTitle("Confirm alert acknowledgment");
        confirmation.setHeaderText("This will update the alert status.");
        confirmation.initOwner(acknowledgeButton.getScene().getWindow());
        Optional<ButtonType> response = confirmation.showAndWait();
        if (response.isEmpty() || response.get() != ButtonType.YES) {
            return;
        }
        try {
            if (alertService.acknowledgeAlert(selected.id())) {
                refresh();
                statusLabel.setText("Alert acknowledged.");
                setError(false);
            } else {
                statusLabel.setText("The alert is no longer active. Refresh the list.");
                setError(true);
            }
        } catch (SQLException e) {
            logger.error("Unable to acknowledge alert {}.", selected.id(), e);
            statusLabel.setText("The alert could not be acknowledged. Check the local database.");
            setError(true);
        }
    }

    private void applyFilters() {
        if (filteredAlerts == null) {
            return;
        }
        String query = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String severity = severityFilter.getValue();
        filteredAlerts.setPredicate(alert ->
                (query.isEmpty()
                        || contains(alert.title(), query)
                        || contains(alert.message(), query)
                        || contains(alert.severity(), query))
                        && ("All severities".equals(severity)
                        || severity == null
                        || severity.equalsIgnoreCase(alert.severity()))
        );
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private void setError(boolean error) {
        statusLabel.getStyleClass().remove("status-error");
        if (error) {
            statusLabel.getStyleClass().add("status-error");
        }
    }
}
