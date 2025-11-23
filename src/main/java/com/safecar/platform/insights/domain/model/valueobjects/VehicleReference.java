package com.safecar.platform.insights.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Identifies the vehicle/driver combination associated with an insight.
 */
@Embeddable
public record VehicleReference(

        @Column(name = "driver_id", nullable = false)
        Long driverId,

        @Column(name = "driver_name", nullable = false, length = 200)
        String driverFullName,

        @Column(name = "vehicle_id", nullable = false)
        Long vehicleId,

        @Column(name = "vehicle_plate", nullable = false, length = 20)
        String plateNumber
) {
    public VehicleReference {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (driverFullName == null || driverFullName.isBlank()) {
            throw new IllegalArgumentException("Driver full name cannot be blank");
        }
        if (plateNumber == null || plateNumber.isBlank()) {
            throw new IllegalArgumentException("Plate number cannot be blank");
        }
    }
}
