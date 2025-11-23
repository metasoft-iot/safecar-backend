package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a diagnostic trouble code.
 */
@Embeddable
public record FaultCode(
        @Column(name = "fault_code", nullable = false, length = 20)
        String code,
        
        @Column(name = "fault_standard", nullable = false, length = 10)
        String standard
) {
    public FaultCode {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Fault code cannot be null or empty");
        }
        if (standard == null || standard.trim().isEmpty()) {
            throw new IllegalArgumentException("Fault standard cannot be null or empty");
        }
    }
}