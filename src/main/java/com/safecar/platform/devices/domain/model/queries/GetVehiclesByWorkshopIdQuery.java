package com.safecar.platform.devices.domain.model.queries;

/**
 * Query to get vehicles by workshop ID.
 * Retrieves all vehicles that have appointments at a specific workshop.
 *
 * @param workshopId the workshop ID
 */
public record GetVehiclesByWorkshopIdQuery(Long workshopId) {
    public GetVehiclesByWorkshopIdQuery {
        if (workshopId == null || workshopId <= 0) {
            throw new IllegalArgumentException("Workshop ID must be a positive value");
        }
    }
}
