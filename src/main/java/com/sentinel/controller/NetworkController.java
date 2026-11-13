package com.sentinel.controller;

import com.sentinel.model.NetworkConnection;
import com.sentinel.service.NetworkMonitoringService;
import com.sentinel.util.DateTimeUtils;
import com.sentinel.util.TableSupport;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class NetworkController {
    private static final Logger logger = LoggerFactory.getLogger(NetworkController.class);
    private final NetworkMonitoringService networkService = new NetworkMonitoringService();
    private final javafx.collections.ObservableList<NetworkConnection> connections = FXCollections.observableArrayList();
    private FilteredList<NetworkConnection> filteredConnections;

    @FXML
    private TextField searchField;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<NetworkConnection> connectionsTable;

    @FXML
    public void initialize() {
        TableSupport.configure(connectionsTable);
        connectionsTable.getColumns().setAll(List.of(
                TableSupport.stringColumn("Observed", connection -> DateTimeUtils.formatDateTime(connection.observedAt()), 145),
                TableSupport.stringColumn("Source", NetworkConnection::sourceIp, 135),
                TableSupport.stringColumn("Destination", NetworkConnection::destinationIp, 135),
                TableSupport.stringColumn("Protocol", NetworkConnection::protocol, 95),
                TableSupport.stringColumn("Status", NetworkConnection::status, 100),
                TableSupport.stringColumn("Transferred", connection -> formatBytes(connection.bytesTransferred()), 100)
        ));
        filteredConnections = new FilteredList<>(connections);
        connectionsTable.setItems(filteredConnections);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        refresh();
    }

    @FXML
    private void refresh() {
        try {
            List<NetworkConnection> records = networkService.getConnections();
            connections.setAll(records);
            statusLabel.setText(records.size() + " locally stored connection record(s)");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to load stored network connections.", e);
            connections.clear();
            statusLabel.setText("Network records could not be loaded. Check the local database.");
            setError(true);
        }
    }

    private void applyFilter() {
        if (filteredConnections == null) {
            return;
        }
        String query = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase(Locale.ROOT);
        filteredConnections.setPredicate(connection -> query.isEmpty()
                || contains(connection.sourceIp(), query)
                || contains(connection.destinationIp(), query)
                || contains(connection.protocol(), query)
                || contains(connection.status(), query));
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format(Locale.ROOT, "%.1f KB", bytes / 1024.0);
        }
        return String.format(Locale.ROOT, "%.1f MB", bytes / (1024.0 * 1024.0));
    }

    private void setError(boolean error) {
        statusLabel.getStyleClass().remove("status-error");
        if (error) {
            statusLabel.getStyleClass().add("status-error");
        }
    }
}
