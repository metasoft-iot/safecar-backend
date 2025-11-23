package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateBusinessProfileResource;

public class CreateBusinessProfileCommandFromResourceAssembler {

    public static CreateBusinessProfileCommand toCommandFromResource(CreateBusinessProfileResource resource) {
        return new CreateBusinessProfileCommand(
                resource.businessName(),
                resource.ruc(),
                resource.businessAddress(),
                resource.contactPhone(),
                resource.contactEmail());
    }
}
