package com.safecar.platform.devices.domain.model.queries;

/**
 * Query to get a Driver by ProfileId
 * 
 * @param profileId the profile ID from Profiles BC
 */
public record GetDriverByProfileIdQuery(Long profileId) {
}