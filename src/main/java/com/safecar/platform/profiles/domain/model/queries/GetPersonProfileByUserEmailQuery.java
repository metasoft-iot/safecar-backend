package com.safecar.platform.profiles.domain.model.queries;

/**
 * Get Person Profile By User Email Query
 * <p>
 *  Query to retrieve a person profile based on the associated user email.
 * </p>
 */
public record GetPersonProfileByUserEmailQuery(String userEmail) {
}
