package com.safecar.platform.profiles.interfaces.rest.transform;

import java.util.Collections;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.interfaces.rest.resource.PersonProfileResource;

/**
 * Person Profile Resource From Entity Assembler
 */
public class PersonProfileResourceFromEntityAssembler {
    /**
     * Transforms the {@link PersonProfile} into a
     * {@link PersonProfileResource}.
     * 
     * @param p the entity to transform
     * @return the transformed resource
     */
    public static PersonProfileResource toResourceFromEntity(PersonProfile p) {
        return new PersonProfileResource(
                p.getId(),
                p.getUserEmail(),
                p.getFullName(),
                p.getCity(),
                p.getCountry(),
                p.getPhoneNumber(),
                p.getDniNumber(),
                " ",
                Collections.emptySet(),
                0);
    }
}
