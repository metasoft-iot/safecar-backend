package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get workshop by business profile ID.
 *
 * @param businessProfileId the business profile ID
 */
public record GetWorkshopByBusinessProfileIdQuery(Long businessProfileId) {
    public GetWorkshopByBusinessProfileIdQuery {
        if (businessProfileId == null || businessProfileId <= 0) {
            throw new IllegalArgumentException("Business Profile ID must be a positive value");
        }
    }
}
