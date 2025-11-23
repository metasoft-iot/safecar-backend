package com.safecar.platform.workshop.interfaces.rest.transform;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.safecar.platform.workshop.domain.model.entities.Specialization;

/**
 * Specialization Set From String Assembler
 * <p>
 * Transforms string sets into Specialization entity sets for domain layer
 * consistency.
 * </p>
 */
public class SpecializationSetFromStringAssembler {

    /**
     * Transforms a Set of String specialization names into a Set of Specialization entities
     * 
     * @param resourceSet the set of string specialization names
     * @return the set of Specialization entities, or empty set if input is null or empty
     */
    public static Set<Specialization> toSpecializationSetFromStringSet(Set<String> resourceSet) {
        if (resourceSet == null || resourceSet.isEmpty()) {
            return Collections.emptySet();
        }
        
        return resourceSet.stream()
                .filter(name -> name != null && !name.trim().isEmpty())
                .map(Specialization::toSpecializationFromName)
                .collect(Collectors.toSet());
    }
}
