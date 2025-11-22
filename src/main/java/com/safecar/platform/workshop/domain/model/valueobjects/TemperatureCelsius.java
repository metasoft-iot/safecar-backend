package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Represents a temperature measurement in Celsius.
 */
@Embeddable
public record TemperatureCelsius(
        @Column(name = "temperature_celsius", precision = 5, scale = 2) BigDecimal value) {
    public TemperatureCelsius {
        if (value == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }
        // Reasonable bounds check for automotive context (-50C to 150C)
        if (value.compareTo(new BigDecimal("-50")) < 0 || value.compareTo(new BigDecimal("150")) > 0) {
            throw new IllegalArgumentException("Temperature must be between -50 and 150 Celsius");
        }
    }
}
