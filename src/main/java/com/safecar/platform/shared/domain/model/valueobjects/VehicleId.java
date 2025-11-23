package com.safecar.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * VehicleId value object
 * <p>
 * Represents a vehicle identifier shared across bounded contexts.
 * Contains both the vehicle ID and plate number for complete identification.
 * This is a shared value object to avoid duplication and ensure consistency.
 * </p>
 * 
 * @since 1.0
 * @author SafeCar Platform Team
 */
@Embeddable
public record VehicleId(
    @NotNull 
    @Column(name = "vehicle_id", nullable = false)
    Long vehicleId,
    
    @NotBlank
    @Column(name = "vehicle_plate_number", nullable = false, length = 20)
    String plateNumber
) {
    
    /**
     * Default constructor for JPA
     */
    public VehicleId() {
        this(1L, "UNKNOWN");
    }
    
    /**
     * Creates a VehicleId from values with validation
     * @param id the vehicle ID
     * @param plate the plate number
     * @return VehicleId instance
     * @throws IllegalArgumentException if values are invalid
     */
    public static VehicleId of(Long id, String plate) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be a positive value, got: " + id);
        }
        if (plate == null || plate.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be null or empty");
        }
        return new VehicleId(id, plate.trim().toUpperCase());
    }
}