package com.safecar.platform.workshop.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;

/**
 * Value Object representing a telemetry timestamp.
 */
@Embeddable
public record TelemetryTimestamp(
        @Column(name = "occurred_at", nullable = false)
        Instant occurredAt
) {
    public TelemetryTimestamp {
        if (occurredAt == null) {
            throw new IllegalArgumentException("Occurred at timestamp cannot be null");
        }
    }
}