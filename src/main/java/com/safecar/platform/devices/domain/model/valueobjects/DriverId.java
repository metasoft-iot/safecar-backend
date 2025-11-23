package com.safecar.platform.devices.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DriverId value object
 * <p>
 * Represents a unique identifier for a Driver within the Devices BC.
 * This follows the pattern similar to ProfileId but for the Driver aggregate.
 * </p>
 */
@Embeddable
public record DriverId(@NotNull @Positive Long driverId) {
    
    public DriverId() {
        this(0L);
    }
}
