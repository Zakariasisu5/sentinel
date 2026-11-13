package com.sentinel.controller;

import com.sentinel.model.IPAddress;
import com.sentinel.service.IPIntelligenceService;
import com.sentinel.util.TableSupport;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class IPIntelligenceController {
    private static final Logger logger = LoggerFactory.getLogger(IPIntelligenceController.class);
    private final IPIntelligenceService intelligenceService = new IPIntelligenceService();

    @FXML
    private TextField searchField;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<IPAddress> addressesTable;

    @FXML
    public void initialize() {
        TableSupport.configure(addressesTable);
        addressesTable.getColumns().setAll(List.of(
                TableSupport.stringColumn("IP address", IPAddress::ipAddress, 145),
                TableSupport.stringColumn("Geography", IPAddress::country, 115),
                TableSupport.stringColumn("Data source", IPAddress::region, 140),
                TableSupport.stringColumn("Reputation", IPAddress::reputation, 220),
                TableSupport.stringColumn("Local threat context",
                        address -> address.suspicious() ? "Open threat record" : "No open threat record", 165)
        ));
        search("");
    }

    @FXML
    private void search() {
        search(searchField.getText());
    }

    @FXML
    private void clearSearch() {
        searchField.clear();
        search("");
    }

    private void search(String query) {
        try {
            List<IPAddress> records = intelligenceService.investigate(query);
            addressesTable.setItems(FXCollections.observableArrayList(records));
            statusLabel.setText(records.size() + " locally observed address(es)");
            setError(false);
        } catch (SQLException e) {
            logger.error("Unable to search local IP intelligence records.", e);
            addressesTable.getItems().clear();
            statusLabel.setText("IP records could not be loaded. Check the local database.");
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
