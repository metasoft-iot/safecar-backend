package com.safecar.platform.workshop.domain.model.queries;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;

/**
 * Query to fetch telemetry samples for a vehicle within a time range.
 */
public record GetTelemetryByVehicleAndRangeQuery(
        VehicleId vehicleId,
        Instant from,
        Instant to
) {
}
