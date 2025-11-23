package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Driver identifier.
 */
@Embeddable
public record DriverId(
        @Column(name = "driver_id", nullable = false) Long driverId) {
    public DriverId {
        if (driverId == null || driverId <= 0)
            throw new IllegalArgumentException("Driver ID must be a positive value");
    }
}