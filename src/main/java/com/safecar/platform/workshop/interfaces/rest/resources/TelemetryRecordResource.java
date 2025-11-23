package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;

/**
 * Simple resource representation for a telemetry record returned by the API.
 */
public record TelemetryRecordResource(
        Long id,
        TelemetrySample sample,
        Instant ingestedAt
) {
}
