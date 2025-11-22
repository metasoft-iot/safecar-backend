package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Represents a humidity measurement as a percentage.
 */
@Embeddable
public record HumidityPercent(
        @Column(name = "humidity_percent", precision = 5, scale = 2) BigDecimal value) {
    public HumidityPercent {
        if (value == null) {
            throw new IllegalArgumentException("Humidity cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Humidity must be between 0 and 100 percent");
        }
    }
}
