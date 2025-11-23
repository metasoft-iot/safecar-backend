package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.interfaces.rest.resource.UpdatePersonProfileResource;

public class UpdatePersonProfileCommandFromResourceAssembler {

    public static UpdatePersonProfileCommand toCommandFromResource(UpdatePersonProfileResource resource) {
        return new UpdatePersonProfileCommand(
                resource.personId(),
                resource.fullName(),
                resource.city(),
                resource.country(),
                new Phone(resource.phone()),
                new Dni(resource.dni()));
    }
}
