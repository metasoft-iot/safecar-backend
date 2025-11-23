package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

/**
 * Resource for creating a new telemetry sample with a flat structure.
 */
public record CreateTelemetryResource(
                Long vehicleId,
                Long driverId,
                String type,
                String severity,
                Instant timestamp,
                Double speed,
                Double latitude,
                Double longitude,
                Double odometer,
                String dtcCode,
                String dtcStandard,
                String cabinGasType,
                Double cabinGasConcentration,
                Double accelerationLateral,
                Double accelerationLongitudinal,
                Double accelerationVertical,
                Double engineTemperature,
                Double cabinTemperature,
                Double cabinHumidity,
                Double electricalCurrent) {
}
