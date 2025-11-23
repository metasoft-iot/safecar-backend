package com.safecar.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Dni Value Object.
 * <p>
 * Represents a national identification number (DNI) consisting of exactly 8
 * digits.
 * Ensures that the DNI is a positive number with exactly 8 digits.
 * </p>
 */
@Embeddable
public record Dni(String dni) {
    public Dni {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("Invalid DNI: must be a positive number with exactly 8 digits");
        }
    }

    public Dni() {
        this("00000000"); // Valor por defecto opcional
    }
}
