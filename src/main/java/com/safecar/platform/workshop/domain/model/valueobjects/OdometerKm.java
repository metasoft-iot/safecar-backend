package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value Object representing odometer reading in kilometers.
 */
@Embeddable
public record OdometerKm(
        @Column(name = "odometer_km", nullable = false, precision = 10, scale = 2)
        BigDecimal value
) {
    public OdometerKm {
        if (value == null) {
            throw new IllegalArgumentException("Odometer value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Odometer cannot be negative");
        }
    }
}