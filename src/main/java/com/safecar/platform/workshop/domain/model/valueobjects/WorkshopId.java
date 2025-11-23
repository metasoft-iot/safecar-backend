package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a Workshop identifier.
 */
@Embeddable
public record WorkshopId(
        @Column(name = "workshop_id", nullable = false) Long workshopId) {
    public WorkshopId {
        if (workshopId == null || workshopId <= 0)
            throw new IllegalArgumentException("Workshop ID must be a positive value");
    }
}