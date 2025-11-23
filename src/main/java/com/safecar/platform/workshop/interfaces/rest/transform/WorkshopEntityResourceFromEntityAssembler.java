package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.interfaces.rest.resources.WorkshopResource;

/**
 * WorkshopEntityResourceFromEntityAssembler
 */
public class WorkshopEntityResourceFromEntityAssembler {

    /**
     * Transforms a Workshop entity to a WorkshopEntityResource
     *
     * @param entity the {@link Workshop} entity
     * @return the {@link WorkshopEntityResource} resource
     */
    public static WorkshopResource toResourceFromEntity(Workshop entity) {
        return new WorkshopResource(
                entity.getId(),
                entity.getBusinessProfileId(),
                entity.getWorkshopDescription(),
                entity.getTotalMechanics());
    }
}