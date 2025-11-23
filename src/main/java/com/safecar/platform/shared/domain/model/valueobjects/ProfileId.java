package com.safecar.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * ProfileId value object
 * <p>
 * Represents a reference to a PersonProfile from the Profiles BC.
 * This enables other BCs to reference person information without tight
 * coupling. This is a shared value object used across multiple bounded contexts
 * to maintain consistency when referencing person profiles.
 * </p>
 */
@Embeddable
public record ProfileId(
        @NotNull @Positive @Column(name = "profile_id", nullable = false) Long profileId) {

    /**
     * Default constructor for JPA
     */
    public ProfileId() {
        this(1L);
    }

    /**
     * Validates that the profile ID is valid for business operations
     * 
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return profileId != null && profileId > 0;
    }

    /**
     * Creates a ProfileId from a Long value with validation
     * 
     * @param id the profile ID
     * @return ProfileId instance
     * @throws IllegalArgumentException if ID is invalid
     */
    public static ProfileId of(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Profile ID must be a positive value, got: " + id);
        }
        return new ProfileId(id);
    }
}