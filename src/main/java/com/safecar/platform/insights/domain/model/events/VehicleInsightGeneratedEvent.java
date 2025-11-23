package com.safecar.platform.insights.domain.model.events;

import java.time.Instant;

public record VehicleInsightGeneratedEvent(
        Long insightId,
        Long vehicleId,
        Long driverId,
        String riskLevel,
        Instant generatedAt
) {
}
