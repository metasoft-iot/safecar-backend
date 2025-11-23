package com.safecar.platform.profiles.domain.model.commands;

import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;

/**
 * Update Person Profile Command
 * <p>
 * This command encapsulates the data required to update a person's profile
 * </p>
 * 
 * @param personId - The unique identifier of the person
 * @param fullName - The full name of the person
 * @param city     - The city where the person resides
 * @param country  - The country where the person resides
 * @param phone    - The phone number of the person
 * @param dni      - The national identification number of the person
 */
public record UpdatePersonProfileCommand(
        Long personId,
        String fullName,
        String city,
        String country,
        Phone phone,
        Dni dni) {

}
