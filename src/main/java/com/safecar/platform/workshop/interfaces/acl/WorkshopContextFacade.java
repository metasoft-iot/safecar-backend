package com.safecar.platform.workshop.interfaces.acl;

import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;

import java.util.Optional;

/**
 * Workshop Context Facade
 * 
 * <p>
 * This interface provides a way to access the workshop context.
 * </p>
 * 
 * Methods:
 * <ul>
 * <li>fetchTelemetrySample(Long telemetryId)</li>
 * </ul>
 */
public interface WorkshopContextFacade {
    /**
     * Method to fetch a telemetry sample by ID.
     * 
     * @param telemetryId The ID of the telemetry sample to fetch.
     * @return An Optional containing the telemetry sample if found, or an empty
     *         Optional if not found.
     */
    Optional<TelemetrySample> fetchTelemetrySample(Long telemetryId);
}
