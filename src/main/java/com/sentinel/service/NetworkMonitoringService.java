package com.sentinel.service;

import com.sentinel.model.NetworkConnection;
import com.sentinel.repository.NetworkConnectionRepository;

import java.util.List;
import java.sql.SQLException;

public class NetworkMonitoringService {
    private final NetworkConnectionRepository repository = new NetworkConnectionRepository();

    public List<NetworkConnection> getConnections() throws SQLException {
        return repository.findRecentConnections();
    }
}
