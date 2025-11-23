package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.UpdateWorkshopCommand;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateWorkshopResource;

/**
 * Assembler to transform UpdateWorkshopResource into UpdateWorkshopCommand.
 */
public class UpdateWorkshopCommandFromResourceAssembler {
    public static UpdateWorkshopCommand toCommandFromResource(Long workshopId, UpdateWorkshopResource resource) {
        return new UpdateWorkshopCommand(workshopId, resource.workshopDescription());
    }
}
