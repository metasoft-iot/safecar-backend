package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents the measured concentration of gases inside the cabin.
 */
@Embeddable
public record CabinGasLevel(

        @Enumerated(EnumType.STRING)
        @Column(name = "cabin_gas_type", length = 40)
        CabinGasType type,

        @Column(name = "cabin_gas_ppm", precision = 10, scale = 2)
        BigDecimal concentrationPpm
) {

    public CabinGasLevel {
        if (type == null) {
            throw new IllegalArgumentException("Cabin gas type cannot be null");
        }
        if (concentrationPpm == null) {
            throw new IllegalArgumentException("Cabin gas concentration cannot be null");
        }
        if (concentrationPpm.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cabin gas concentration must be positive");
        }
    }
}
