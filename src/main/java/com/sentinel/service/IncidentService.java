package com.sentinel.service;

import com.sentinel.model.Incident;
import com.sentinel.repository.IncidentRepository;

import java.util.List;
import java.sql.SQLException;

public class IncidentService {
    private final IncidentRepository repository = new IncidentRepository();

    public List<Incident> getIncidentHistory() throws SQLException {
        return repository.findOpenIncidents();
    }

    public boolean resolveIncident(String incidentId) throws SQLException {
        return repository.resolve(incidentId);
    }
}
