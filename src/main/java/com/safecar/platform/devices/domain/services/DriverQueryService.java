package com.safecar.platform.devices.domain.services;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.queries.GetDriverByProfileIdQuery;

import java.util.Optional;

/**
 * Driver Query Service Interface
 * <p>
 * This interface defines the contract for handling driver-related queries
 * within the Devices bounded context.
 * </p>
 */
public interface DriverQueryService {
    /**
     * Handles the query to get a driver by profile ID.
     * 
     * @param query the query containing the profile ID
     * @return an Optional containing the found Driver, or empty if not found
     */
    Optional<Driver> handle(GetDriverByProfileIdQuery query);
}