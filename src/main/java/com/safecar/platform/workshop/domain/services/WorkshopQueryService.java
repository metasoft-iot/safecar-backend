package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;

import java.util.List;
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
    
    /**
     * Handle the retrieval of all Workshops.
     * 
     * @param query the {@link GetAllWorkshopsQuery} instance
     * @return a {@link List} of {@link Workshop}
     */
    List<Workshop> handle(GetAllWorkshopsQuery query);
}