package com.safecar.platform.workshop.interfaces.rest.resources;

import java.util.Set;

/**
 * Update Mechanic Metrics Resource
 * <p>
 * REST resource representing the data required to update a mechanic's metrics.
 * </p>
 * 
 * @param specializations   set of specializations of the mechanic
 * @param yearsOfExperience years of professional experience
 */
public record UpdateMechanicMetricsResource(
                Set<String> specializations,
                Integer yearsOfExperience) {
}
