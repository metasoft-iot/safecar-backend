package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.interfaces.rest.resource.CreatePersonProfileResource;

/**
 * Create Person Profile Command From Resource Assembler
 * <p>
 * Transforms REST resources into domain commands for clean separation of concerns.
 * </p>
 */
public class CreatePersonProfileCommandFromResourceAssembler {
    /**
     * Transforms the {@link CreatePersonProfileResource} into a
     * {@link CreatePersonProfileCommand}.
     * 
     * @param r the resource to transform
     * @return the transformed command
     */
    public static CreatePersonProfileCommand toCommandFromResource(CreatePersonProfileResource r) {
        return new CreatePersonProfileCommand(
                r.fullName(),
                r.city(),
                r.country(),
                r.phone(),
                r.dni());
    }
}
