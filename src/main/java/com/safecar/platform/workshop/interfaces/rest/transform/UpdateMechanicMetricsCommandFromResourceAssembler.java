package com.safecar.platform.workshop.interfaces.rest.transform;

import java.util.Set;

import com.safecar.platform.workshop.domain.model.commands.UpdateMechanicMetricsCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateMechanicMetricsResource;

/**
 * Update Mechanic Metrics Command From Resource Assembler
 * <p>
 * Transforms REST resources into domain commands for clean separation of
 * concerns.
 * 
 * NOTE: This assembler creates transient Specialization entities.
 * The CommandService is responsible for fetching the persisted entities
 * from the database before updating the Mechanic.
 * </p>
 */
public class UpdateMechanicMetricsCommandFromResourceAssembler {

    /**
     * Transforms the {@link UpdateMechanicMetricsResource} into a
     * {@link UpdateMechanicMetricsCommand}
     * 
     * @param resource the resource to transform
     * @return the transformed command with transient Specialization entities
     */
    public static UpdateMechanicMetricsCommand toCommandFromResource(UpdateMechanicMetricsResource resource) {
        // Create transient specializations - CommandService will fetch persisted ones
        Set<Specialization> transientSpecializations = SpecializationSetFromStringAssembler
                .toSpecializationSetFromStringSet(resource.specializations());

        return new UpdateMechanicMetricsCommand(
                transientSpecializations,
                resource.yearsOfExperience());
    }
}