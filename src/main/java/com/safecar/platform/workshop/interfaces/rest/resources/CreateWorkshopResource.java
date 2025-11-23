package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Create Workshop Resource
 * <p>
 * Workshop name, address, and phone are obtained from the associated BusinessProfile.
 * Only workshop-specific information needs to be provided.
 * </p>
 * 
 * @param businessProfileId the business profile ID
 * @param workshopDescription the workshop description (optional)
 */
public record CreateWorkshopResource(Long businessProfileId,
                                   String workshopDescription) {
}