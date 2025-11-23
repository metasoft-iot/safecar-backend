package com.safecar.platform.workshop.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryAlertsBySeverityQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleAndRangeQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByVehicleQuery;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryRecordByIdQuery;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleTelemetryQueryServiceImpl implements VehicleTelemetryQueryService {

    private final TelemetryRecordRepository repository;

    public VehicleTelemetryQueryServiceImpl(TelemetryRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TelemetryRecord> handle(GetTelemetryRecordByIdQuery query) {
        return repository.findById(query.recordId());
    }

    @Override
    public List<TelemetryRecord> handle(GetTelemetryByVehicleAndRangeQuery query) {
        return repository.findBySampleVehicleIdAndIngestedAtBetween(query.vehicleId(), query.from(), query.to());
    }

    @Override
    public List<TelemetryRecord> handle(GetTelemetryByVehicleQuery query) {
        return repository.findBySampleVehicleIdOrderByIngestedAtDesc(query.vehicleId());
    }

    @Override
    public List<TelemetryRecord> handle(GetTelemetryAlertsBySeverityQuery query) {
        return repository.findBySampleSeverityAndIngestedAtBetween(query.severity(), query.from(), query.to());
    }
}
