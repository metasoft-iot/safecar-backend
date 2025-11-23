package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicByProfileIdQuery;

import java.util.Optional;

/**
 * Mechanic Query Service Interface
 * <p>
 * This interface defines the contract for handling mechanic-related queries
 * within the Workshop bounded context.
 * </p>
 */
public interface MechanicQueryService {
    /**
     * Handles the query to get a mechanic by profile ID.
     * 
     * @param query the query containing the profile ID
     * @return an Optional containing the found Mechanic, or empty if not found
     */
    Optional<Mechanic> handle(GetMechanicByProfileIdQuery query);

    /**
     * Handles the query to get mechanics by workshop ID.
     * 
     * @param query the query containing the workshop ID
     * @return a List of found Mechanics
     */
    java.util.List<Mechanic> handle(
            com.safecar.platform.workshop.domain.model.queries.GetMechanicsByWorkshopIdQuery query);
}