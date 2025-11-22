package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Represents an electrical current measurement in Amperes.
 */
@Embeddable
public record ElectricalCurrentAmps(
        @Column(name = "current_amps", precision = 6, scale = 3) BigDecimal value) {
    public ElectricalCurrentAmps {
        if (value == null) {
            throw new IllegalArgumentException("Current cannot be null");
        }
    }
}
