package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;

import java.util.Optional;

/**
 * Workshop Query Service
 */
public interface WorkshopQueryService {

    /**
     * Handle the retrieval of a Workshop by its ID.
     * 
     * @param query the {@link GetWorkshopByIdQuery} instance
     * @return an {@link Optional} of {@link Workshop} if found
     */
    Optional<Workshop> handle(GetWorkshopByIdQuery query);

    /**
     * Handle the retrieval of a Workshop by its business profile ID.
     * 
     * @param query the
     *              {@link com.safecar.platform.workshop.domain.model.queries.GetWorkshopByBusinessProfileIdQuery}
     *              instance
     * @return an {@link Optional} of {@link Workshop} if found
     */
    Optional<Workshop> handle(
            com.safecar.platform.workshop.domain.model.queries.GetWorkshopByBusinessProfileIdQuery query);
}