package com.safecar.platform.devices.domain.model.queries;

/**
 * Get Vehicle by ID Query - Query to get a vehicle by id.
 * 
 * @param vehicleId the id of the vehicle
 */
public record GetVehicleByIdQuery(Long vehicleId) {

    public GetVehicleByIdQuery {
        if (vehicleId <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be a positive non-null value.");
        }
    }
}
