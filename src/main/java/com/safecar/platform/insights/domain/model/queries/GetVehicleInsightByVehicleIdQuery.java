package com.safecar.platform.insights.domain.model.queries;

public record GetVehicleInsightByVehicleIdQuery(Long vehicleId) {
    public GetVehicleInsightByVehicleIdQuery {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle id cannot be null");
        }
    }
}
