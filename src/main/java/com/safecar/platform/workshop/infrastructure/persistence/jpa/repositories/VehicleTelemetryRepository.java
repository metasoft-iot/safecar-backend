package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.aggregates.VehicleTelemetry;

import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import java.util.Optional;

@Repository
public interface VehicleTelemetryRepository extends JpaRepository<VehicleTelemetry, Long> {
    Optional<VehicleTelemetry> findByVehicleId(VehicleId vehicleId);
}
