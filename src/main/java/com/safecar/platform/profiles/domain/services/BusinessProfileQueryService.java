package com.safecar.platform.profiles.domain.services;

import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByUserEmailQuery;

import java.util.Optional;

/**
 * Business Profile Query Service
 */
public interface BusinessProfileQueryService {

    /**
     * Handle the retrieval of a BusinessProfile by its ID.
     * 
     * @param query the {@link GetBusinessProfileByIdQuery} instance
     * @return an {@link Optional} of {@link BusinessProfile} if found
     */
    Optional<BusinessProfile> handle(GetBusinessProfileByIdQuery query);

    /**
     * Handle the retrieval of a BusinessProfile by user email.
     * 
     * @param query the {@link GetBusinessProfileByUserEmailQuery} instance
     * @return an {@link Optional} of {@link BusinessProfile} if found
     */
    Optional<BusinessProfile> handle(GetBusinessProfileByUserEmailQuery query);
}