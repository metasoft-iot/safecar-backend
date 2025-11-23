package com.safecar.platform.insights.interfaces.rest.transform;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.interfaces.rest.resources.VehicleInsightResource;

public class VehicleInsightResourceFromEntityAssembler {

    public static VehicleInsightResource toResource(VehicleInsight insight) {
        return new VehicleInsightResource(
                insight.getId(),
                insight.getVehicle().vehicleId(),
                insight.getVehicle().driverId(),
                insight.getVehicle().driverFullName(),
                insight.getVehicle().plateNumber(),
                insight.getRiskLevel(),
                insight.getMaintenancePrediction().summary(),
                insight.getMaintenancePrediction().recommendedWindow(),
                insight.getDrivingHabits().summary(),
                insight.getDrivingHabits().habitScore(),
                insight.getDrivingHabits().alerts(),
                insight.getGeneratedAt(),
                insight.getRecommendations().stream()
                        .map(rec -> new VehicleInsightResource.RecommendationResource(rec.title(), rec.detail()))
                        .toList()
        );
    }
}
