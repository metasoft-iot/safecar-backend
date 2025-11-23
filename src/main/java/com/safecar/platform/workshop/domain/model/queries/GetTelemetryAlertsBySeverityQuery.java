package com.safecar.platform.workshop.domain.model.queries;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.AlertSeverity;

/**
 * Query to fetch telemetry alerts filtered by severity in a time range.
 */
public record GetTelemetryAlertsBySeverityQuery(
        AlertSeverity severity,
        Instant from,
        Instant to
) {
}
