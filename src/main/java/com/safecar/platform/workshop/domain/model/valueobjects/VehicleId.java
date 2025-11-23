package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Vehicle identifier.
 */
@Embeddable
public record VehicleId(
        @Column(name = "vehicle_id", nullable = false)
        Long vehicleId
) {
    public VehicleId {
        if (vehicleId == null || vehicleId <= 0) 
            throw new IllegalArgumentException("Vehicle ID must be a positive value");
    }
}