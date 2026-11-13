package com.sentinel.service;

import com.sentinel.model.IPAddress;
import com.sentinel.repository.IPAddressRepository;

import java.sql.SQLException;
import java.util.List;

public class IPIntelligenceService {
    private final IPAddressRepository repository = new IPAddressRepository();

    public List<IPAddress> investigate(String ipAddress) throws SQLException {
        return repository.findLocallyObservedAddresses(ipAddress);
    }
}
