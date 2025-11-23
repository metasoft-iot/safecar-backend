package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get mechanics by workshop ID.
 *
 * @param workshopId the workshop ID
 */
public record GetMechanicsByWorkshopIdQuery(Long workshopId) {
    public GetMechanicsByWorkshopIdQuery {
        if (workshopId == null || workshopId <= 0) {
            throw new IllegalArgumentException("Workshop ID must be a positive value");
        }
    }
}
