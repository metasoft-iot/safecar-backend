package com.safecar.platform.workshop.interfaces.rest.resources;

import java.util.Set;

/**
 * Mechanic Resource
 * <p>
 * REST resource representation of a Mechanic within the Workshop bounded
 * context.
 * </p>
 * 
 * @param id                the mechanic ID
 * @param profileId         the profile ID from Profiles BC
 * @param workshopId        the workshop ID (null if not assigned yet)
 * @param specializations   set of specializations of the mechanic
 * @param yearsOfExperience years of professional experience
 */
public record MechanicResource(
                Long id,
                Long profileId,
                String fullName,
                Long workshopId,
                Set<String> specializations,
                Integer yearsOfExperience) {

}
