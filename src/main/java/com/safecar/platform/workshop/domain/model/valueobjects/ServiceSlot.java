package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;

/**
 * Value Object representing a Service Slot with start and end times.
 */
@Embeddable
public record ServiceSlot(
        @Column(name = "start_at", nullable = false)
        Instant startAt,
        
        @Column(name = "end_at", nullable = false)
        Instant endAt
) {
    public ServiceSlot {
        if (startAt == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (endAt == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (endAt.isBefore(startAt)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (startAt.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Start time must be in the future");
        }
    }
    
    /**
     * Checks if this service slot overlaps with another service slot.
     */
    public boolean overlapsWith(ServiceSlot other) {
        return startAt.isBefore(other.endAt) && endAt.isAfter(other.startAt);
    }
    
    /**
     * Gets the duration of the service slot in minutes.
     */
    public long getDurationInMinutes() {
        return java.time.Duration.between(startAt, endAt).toMinutes();
    }
}