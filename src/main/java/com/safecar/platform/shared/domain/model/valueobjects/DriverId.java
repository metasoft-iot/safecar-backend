package com.safecar.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DriverId value object
 * <p>
 * Represents a driver identifier shared across bounded contexts.
 * This is a shared value object to avoid duplication and ensure consistency
 * when referencing drivers from different contexts.
 * </p>
 * 
 * @since 1.0
 * @author SafeCar Platform Team
 */
@Embeddable
public record DriverId(
        @NotNull @Positive @Column(name = "driver_id", nullable = false) Long driverId) {

    /**
     * Default constructor for JPA
     */
    public DriverId() {
        this(1L);
    }

    /**
     * Creates a DriverId from a Long value with validation
     * 
     * @param id the driver ID
     * @return DriverId instance
     * @throws IllegalArgumentException if ID is invalid
     */
    public static DriverId of(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Driver ID must be a positive value, got: " + id);
        }
        return new DriverId(id);
    }
}