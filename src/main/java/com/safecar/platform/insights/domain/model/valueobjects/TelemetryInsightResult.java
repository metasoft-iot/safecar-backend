package com.safecar.platform.insights.domain.model.valueobjects;

import java.time.Instant;
import java.util.List;

/**
 * Telemetry Insight Result
 * 
 * <p>
 * This class is a record that represents the result of a telemetry insight.
 * It contains the risk level, maintenance summary, maintenance window,
 * driving habit score, driving habit summary, driving alerts, recommendations,
 * LLM response ID, and generated at.
 * </p>
 * 
 * @param riskLevel the risk level of the vehicle
 * @param maintenanceSummary the maintenance summary of the vehicle
 * @param maintenanceWindow the maintenance window of the vehicle
 * @param drivingHabitScore the driving habit score of the vehicle
 * @param drivingHabitSummary the driving habit summary of the vehicle
 * @param drivingAlerts the driving alerts of the vehicle
 * @param recommendations the recommendations of the vehicle
 * @param llmResponseId the LLM response ID of the vehicle
 * @param generatedAt the generated at of the vehicle
 * 
 */
public record TelemetryInsightResult(
        String riskLevel,
        String maintenanceSummary,
        String maintenanceWindow,
        Integer drivingHabitScore,
        String drivingHabitSummary,
        String drivingAlerts,
        List<InsightRecommendation> recommendations,
        String llmResponseId,
        Instant generatedAt
) {
}
