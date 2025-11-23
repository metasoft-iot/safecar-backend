package com.safecar.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Phone Value Objects
 * <p>
 * Represents a phone number with validation to ensure it is a positive number
 * consisting of exactly 9 digits.
 * </p>
 */
@Embeddable
public record Phone(String phone) {

    public Phone {
        if (phone == null || !phone.matches("\\d{9}")) {
            throw new IllegalArgumentException("Invalid Phone: must be a positive number with exactly 9 digits");
        }
    }

    public Phone() {
        this("000000000"); // Valor por defecto opcional
    }
}
