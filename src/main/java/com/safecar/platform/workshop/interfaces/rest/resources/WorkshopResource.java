package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Workshop Entity Resource
 * <p>
 * Workshop name, address, and phone are obtained from the associated
 * BusinessProfile.
 * This resource combines Workshop entity data with BusinessProfile data for API
 * responses.
 * </p>
 * 
 * @param businessProfileId   the business profile ID
 * @param workshopDescription the workshop description (from Workshop)
 * @param totalMechanics      the total number of mechanics
 */
public record WorkshopResource(Long id,
                Long businessProfileId,
                String businessName,
                String businessAddress,
                String workshopDescription,
                int totalMechanics) {
}