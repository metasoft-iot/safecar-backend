package com.safecar.platform.workshop.domain.model.commands;

import java.util.Set;

import com.safecar.platform.workshop.domain.model.entities.Specialization;

/**
 * Update Mechanic Command
 * <p>
 *  This command contains updated workshop-specific information for a mechanic.
 * </p>
 * @param specializations set of specializations of the mechanic
 * @param yearsOfExperience years of professional experience
 */
public record UpdateMechanicMetricsCommand(
        Set<Specialization> specializations,
        Integer yearsOfExperience) {
}
