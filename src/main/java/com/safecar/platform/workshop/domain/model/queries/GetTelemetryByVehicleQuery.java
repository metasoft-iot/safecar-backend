package com.safecar.platform.workshop.domain.model.queries;

import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;

/**
 * Query to fetch all telemetry samples for a vehicle (ordered by ingested date, most recent first).
 */
public record GetTelemetryByVehicleQuery(
        VehicleId vehicleId
) {
}

