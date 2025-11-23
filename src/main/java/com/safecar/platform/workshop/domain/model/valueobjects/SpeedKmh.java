package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value Object representing speed in kilometers per hour.
 */
@Embeddable
public record SpeedKmh(
        @Column(name = "speed_kmh", nullable = false, precision = 6, scale = 2)
        BigDecimal value
) {
    public SpeedKmh {
        if (value == null) {
            throw new IllegalArgumentException("Speed value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Speed cannot be negative");
        }
    }
}