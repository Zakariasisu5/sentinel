package com.sentinel.controller;

import com.sentinel.model.Incident;
import com.sentinel.service.IncidentService;
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

public class IncidentController {
    private static final Logger logger = LoggerFactory.getLogger(IncidentController.class);
    private final IncidentService incidentService = new IncidentService();
    private final javafx.collections.ObservableList<Incident> incidents = FXCollections.observableArrayList();
    private FilteredList<Incident> filteredIncidents;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> severityFilter;
    @FXML
    private Button resolveButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Incident> incidentsTable;

    @FXML
    public void initialize() {
        TableSupport.configure(incidentsTable);
        incidentsTable.getColumns().setAll(List.of(
                TableSupport.stringColumn("Created", incident -> DateTimeUtils.formatDateTime(incident.createdAt()), 145),
                TableSupport.stringColumn("Severity", Incident::severity, 90),
                TableSupport.stringColumn("Incident", Incident::title, 165),
                TableSupport.stringColumn("Assigned to", incident -> valueOrDash(incident.assignedTo()), 120),
                TableSupport.stringColumn("Description", Incident::description, 230),
                TableSupport.stringColumn("Status", Incident::status, 90)
        ));
        severityFilter.setItems(FXCollections.observableArrayList(
                "All severities", "Critical", "High", "Medium", "Low"
        ));
        severityFilter.getSelectionModel().selectFirst();
        filteredIncidents = new FilteredList<>(incidents);
        incidentsTable.setItems(filteredIncidents);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        severityFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        incidentsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> resolveButton.setDisable(selected == null));
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            List<Incident> records = incidentService.getIncidentHistory();
            incidents.setAll(records);
            statusLabel.setText(records.size() + " open incident(s)");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to load open incidents.", e);
            incidents.clear();
            statusLabel.setText("Incident records could not be loaded. Check the local database.");
            setError(true);
        }
    }

    @FXML
    private void resolveSelected() {
        Incident selected = incidentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Resolve \"" + selected.title() + "\"?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirmation.setTitle("Confirm incident resolution");
        confirmation.setHeaderText("This will close the incident.");
        confirmation.initOwner(resolveButton.getScene().getWindow());
        Optional<ButtonType> response = confirmation.showAndWait();
        if (response.isEmpty() || response.get() != ButtonType.YES) {
            return;
        }
        try {
            if (incidentService.resolveIncident(selected.id())) {
                refresh();
                statusLabel.setText("Incident resolved.");
                setError(false);
            } else {
                statusLabel.setText("The incident is no longer open. Refresh the list.");
                setError(true);
            }
        } catch (SQLException e) {
            logger.error("Unable to resolve incident {}.", selected.id(), e);
            statusLabel.setText("The incident could not be resolved. Check the local database.");
            setError(true);
        }
    }

    private void applyFilters() {
        if (filteredIncidents == null) {
            return;
        }
        String query = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String severity = severityFilter.getValue();
        filteredIncidents.setPredicate(incident ->
                (query.isEmpty()
                        || contains(incident.title(), query)
                        || contains(incident.description(), query)
                        || contains(incident.assignedTo(), query))
                        && ("All severities".equals(severity)
                        || severity == null
                        || severity.equalsIgnoreCase(incident.severity()))
        );
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }

    private void setError(boolean error) {
        statusLabel.getStyleClass().remove("status-error");
        if (error) {
            statusLabel.getStyleClass().add("status-error");
        }
    }
}
