package com.safecar.platform.insights.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record VehicleInsightResource(
        Long insightId,
        Long vehicleId,
        Long driverId,
        String driverFullName,
        String plateNumber,
        String riskLevel,
        String maintenanceSummary,
        String maintenanceWindow,
        String drivingSummary,
        Integer drivingScore,
        String drivingAlerts,
        Instant generatedAt,
        List<RecommendationResource> recommendations
) {
    public record RecommendationResource(String title, String detail) {
    }
}
