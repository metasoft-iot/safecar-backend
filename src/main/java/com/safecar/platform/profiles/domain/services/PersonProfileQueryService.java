package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserEmailQuery;

import java.util.Optional;

/**
 * Person Profile Query Service
 * <p>
 * Service interface for handling person profile-related query operations.
 * </p>
 */
public interface PersonProfileQueryService {
    /**
     * Handles the query to find a PersonProfile by the associated user email.
     * 
     * @param query the {@link GetPersonProfileByUserEmailQuery} containing the user email
     * @return an {@link Optional} containing the found {@link PersonProfile}, or
     *         empty if not found
     */
    Optional<PersonProfile> handle(GetPersonProfileByUserEmailQuery query);

    /**
     * Handles the query to find a PersonProfile by its ID.
     * 
     * @param query the {@link GetPersonProfileByIdQuery} containing the profile ID
     * @return an {@link Optional} containing the found {@link PersonProfile}, or
     *         empty if not found
     */
    Optional<PersonProfile> handle(GetPersonProfileByIdQuery query);
}
