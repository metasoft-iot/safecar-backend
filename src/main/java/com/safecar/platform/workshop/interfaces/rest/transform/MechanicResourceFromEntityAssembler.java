package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.interfaces.rest.resources.MechanicResource;

/**
 * Mechanic Resource From Entity Assembler
 * <p>
 * Assembles MechanicResource instances from Mechanic domain entities.
 * </p>
 */
public class MechanicResourceFromEntityAssembler {
    /**
     * Converts a Mechanic domain entity to a MechanicResource.
     * 
     * @param entity the Mechanic domain entity
     * @return the MechanicResource
     */
    public static MechanicResource toResourceFromEntity(Mechanic entity, String fullName) {

        var specializationNames = SpecializationStringSetFromSetAssembler
                .toStringSetFromSpecializationSet(entity.getSpecializations());

        return new MechanicResource(
                entity.getId(),
                entity.getProfileId(),
                fullName,
                entity.getWorkshopIdValue(),
                specializationNames,
                entity.getYearsOfExperience());
    }
}
