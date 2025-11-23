package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Captures a 3-axis acceleration sensor reading expressed in G forces.
 */
@Embeddable
public record AccelerationVector(

        @Column(name = "accel_lateral_g", precision = 6, scale = 3)
        BigDecimal lateralG,

        @Column(name = "accel_longitudinal_g", precision = 6, scale = 3)
        BigDecimal longitudinalG,

        @Column(name = "accel_vertical_g", precision = 6, scale = 3)
        BigDecimal verticalG
) {
    public AccelerationVector {
        validate(lateralG, "lateral");
        validate(longitudinalG, "longitudinal");
        validate(verticalG, "vertical");
    }

    private static void validate(BigDecimal value, String axis) {
        if (value == null) return;
        if (value.abs().compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Acceleration on " + axis + " axis exceeds safe range (+/-5g)");
        }
    }

    public boolean isHarshEvent() {
        return exceedsThreshold(lateralG) || exceedsThreshold(longitudinalG);
    }

    private boolean exceedsThreshold(BigDecimal value) {
        return value != null && value.abs().compareTo(BigDecimal.valueOf(1.2)) > 0;
    }
}
