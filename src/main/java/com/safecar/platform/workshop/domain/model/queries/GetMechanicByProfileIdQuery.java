package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get a Mechanic by ProfileId
 * 
 * @param profileId the profile ID from Profiles BC
 */
public record GetMechanicByProfileIdQuery(Long profileId) {
}