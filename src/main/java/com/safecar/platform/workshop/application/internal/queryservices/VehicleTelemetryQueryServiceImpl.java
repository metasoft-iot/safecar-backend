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

import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByIdQuery;
import com.safecar.platform.deviceManagement.domain.services.DeviceQueryService;
import com.safecar.platform.workshop.domain.model.queries.GetTelemetryByDeviceIdQuery;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;

import java.util.Collections;

@Service
public class VehicleTelemetryQueryServiceImpl implements VehicleTelemetryQueryService {

    private final TelemetryRecordRepository repository;
    private final DeviceQueryService deviceQueryService;

    public VehicleTelemetryQueryServiceImpl(TelemetryRecordRepository repository,
            DeviceQueryService deviceQueryService) {
        this.repository = repository;
        this.deviceQueryService = deviceQueryService;
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

    @Override
    public List<TelemetryRecord> handle(GetTelemetryByDeviceIdQuery query) {
        var deviceQuery = new GetDeviceByIdQuery(query.deviceId());
        var device = deviceQueryService.handle(deviceQuery);

        if (device.isPresent() && device.get().getVehicleId() != null) {
            var vehicleId = new VehicleId(device.get().getVehicleId());
            return repository.findBySampleVehicleIdOrderByIngestedAtDesc(vehicleId);
        }

        return Collections.emptyList();
    }
}
