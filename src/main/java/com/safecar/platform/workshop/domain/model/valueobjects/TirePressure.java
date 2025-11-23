package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Captures tire pressure for each wheel in PSI in order to detect
 * leaks or imbalances that impact driving safety.
 */
@Embeddable
public record TirePressure(
        @Column(name = "front_left_tire_psi", precision = 5, scale = 2)
        BigDecimal frontLeft,

        @Column(name = "front_right_tire_psi", precision = 5, scale = 2)
        BigDecimal frontRight,

        @Column(name = "rear_left_tire_psi", precision = 5, scale = 2)
        BigDecimal rearLeft,

        @Column(name = "rear_right_tire_psi", precision = 5, scale = 2)
        BigDecimal rearRight
) {

    public TirePressure {
        validatePressure(frontLeft, "frontLeft");
        validatePressure(frontRight, "frontRight");
        validatePressure(rearLeft, "rearLeft");
        validatePressure(rearRight, "rearRight");
    }

    private static void validatePressure(BigDecimal value, String position) {
        if (value == null) return; // allow partial sensor readings
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(position + " tire pressure must be greater than zero");
        }
        if (value.compareTo(BigDecimal.valueOf(80)) > 0) {
            throw new IllegalArgumentException(position + " tire pressure cannot exceed 80 PSI");
        }
    }
}
