package com.safecar.platform.insights.domain.model.commands;

/**
 * Generate Vehicle Insight Command
 * 
 * <p>
 * This class is a record that represents a command to generate a vehicle
 * insight.
 * </p>
 * 
 * @param telemetryId the telemetry ID of the vehicle to generate the insight
 *                    for
 * 
 */
public record GenerateVehicleInsightCommand(
                Long telemetryId) {
}
