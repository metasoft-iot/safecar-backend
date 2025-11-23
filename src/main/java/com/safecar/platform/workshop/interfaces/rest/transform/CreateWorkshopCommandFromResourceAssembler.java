package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateWorkshopResource;

/**
 * CreateWorkshopCommandFromResourceAssembler
 */
public class CreateWorkshopCommandFromResourceAssembler {

    /**
     * Transforms a CreateWorkshopResource to a CreateWorkshopCommand
     *
     * @param resource the {@link CreateWorkshopResource} resource
     * @return the {@link CreateWorkshopCommand} command
     */
    public static CreateWorkshopCommand toCommandFromResource(CreateWorkshopResource resource) {
        return new CreateWorkshopCommand(
                resource.businessProfileId(),
                resource.workshopDescription()
        );
    }
}