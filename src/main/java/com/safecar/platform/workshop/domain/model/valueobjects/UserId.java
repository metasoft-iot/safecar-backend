package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a User identifier.
 */
@Embeddable
public record UserId(
        @Column(name = "user_id", nullable = false)
        Long userId
) {
    public UserId {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive value");
        }
    }
}