package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;

import java.time.Instant;
import java.util.List;

@Repository
public interface TelemetryRecordRepository extends JpaRepository<TelemetryRecord, Long> {
    List<TelemetryRecord> findBySampleVehicleIdAndIngestedAtBetween(VehicleId vehicleId, Instant from, Instant to);
    List<TelemetryRecord> findBySampleSeverityAndIngestedAtBetween(com.safecar.platform.workshop.domain.model.valueobjects.AlertSeverity severity, Instant from, Instant to);
    List<TelemetryRecord> findBySampleVehicleIdOrderByIngestedAtDesc(VehicleId vehicleId);

    long countByTelemetryAggregateId(Long telemetryAggregateId);

    void deleteByTelemetryAggregateId(Long telemetryAggregateId);
}
