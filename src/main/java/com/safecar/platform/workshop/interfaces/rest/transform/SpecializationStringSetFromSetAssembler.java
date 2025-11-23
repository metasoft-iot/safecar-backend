package com.safecar.platform.workshop.interfaces.rest.transform;

import java.util.Set;
import java.util.stream.Collectors;

import com.safecar.platform.workshop.domain.model.entities.Specialization;

/**
 * Specialization String Set From Set Assembler
 * <p>
 * Transforms a set of Specialization entities into a set of specialization
 * names (strings)
 * </p>
 */
public class SpecializationStringSetFromSetAssembler {

    /**
     * Transforms a set of Specialization entities into a set of specialization
     * names (strings)
     * 
     * @param specializationSet the set of Specialization entities
     * @return the set of specialization names
     */
    public static Set<String> toStringSetFromSpecializationSet(Set<Specialization> specializationSet) {
        return specializationSet.stream()
                .map(Specialization::getStringName)
                .collect(Collectors.toSet());
    }
}
