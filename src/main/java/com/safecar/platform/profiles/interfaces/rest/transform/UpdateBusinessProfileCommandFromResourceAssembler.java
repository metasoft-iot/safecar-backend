package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.UpdateBusinessProfileCommand;
import com.safecar.platform.profiles.interfaces.rest.resource.UpdateBusinessProfileResource;

public class UpdateBusinessProfileCommandFromResourceAssembler {

    public static UpdateBusinessProfileCommand toCommandFromResource(UpdateBusinessProfileResource resource) {
        return new UpdateBusinessProfileCommand(
                resource.businessName(),
                resource.ruc(),
                resource.businessAddress(),
                resource.contactPhone(),
                resource.contactEmail(),
                resource.description());
    }
}
