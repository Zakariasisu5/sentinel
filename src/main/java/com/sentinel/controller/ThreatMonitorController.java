package com.sentinel.controller;

import com.sentinel.model.Threat;
import com.sentinel.repository.ThreatRepository;
import com.sentinel.util.DateTimeUtils;
import com.sentinel.util.TableSupport;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class ThreatMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(ThreatMonitorController.class);
    private final ThreatRepository repository = new ThreatRepository();
    private final javafx.collections.ObservableList<Threat> threats = FXCollections.observableArrayList();
    private FilteredList<Threat> filteredThreats;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> severityFilter;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Threat> threatsTable;

    @FXML
    public void initialize() {
        TableSupport.configure(threatsTable);
        threatsTable.getColumns().setAll(List.of(
                TableSupport.stringColumn("Detected", threat -> DateTimeUtils.formatDateTime(threat.detectedAt()), 145),
                TableSupport.stringColumn("Severity", Threat::severity, 90),
                TableSupport.stringColumn("Threat", Threat::title, 165),
                TableSupport.stringColumn("Source IP", Threat::sourceIp, 125),
                TableSupport.stringColumn("Description", Threat::description, 230),
                TableSupport.stringColumn("Status", Threat::status, 90)
        ));
        severityFilter.setItems(FXCollections.observableArrayList(
                "All severities", "Critical", "High", "Medium", "Low"
        ));
        severityFilter.getSelectionModel().selectFirst();
        filteredThreats = new FilteredList<>(threats);
        threatsTable.setItems(filteredThreats);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        severityFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            List<Threat> records = repository.findOpenThreats();
            threats.setAll(records);
            statusLabel.setText(records.size() + " open threat record(s)");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to load open threats.", e);
            threats.clear();
            statusLabel.setText("Threat records could not be loaded. Check the local database.");
            setError(true);
        }
    }

    private void applyFilters() {
        if (filteredThreats == null) {
            return;
        }
        String query = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String severity = severityFilter.getValue();
        filteredThreats.setPredicate(threat ->
                (query.isEmpty()
                        || contains(threat.title(), query)
                        || contains(threat.sourceIp(), query)
                        || contains(threat.description(), query))
                        && ("All severities".equals(severity)
                        || severity == null
                        || severity.equalsIgnoreCase(threat.severity()))
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
