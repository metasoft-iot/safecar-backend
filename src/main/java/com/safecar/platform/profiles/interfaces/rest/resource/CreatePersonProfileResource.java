package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Create Person Profile Resource
 * <p>
 * Resource for creating a basic person profile with essential personal information.
 * Driver and Mechanic profiles are created automatically via events.
 * </p>
 * 
 * @param fullName Full name of the person.
 * @param city     City of residence.
 * @param country  Country of residence.
 * @param phone    Contact phone number (9 digits).
 * @param dni      National identification number (8 digits).
 */
public record CreatePersonProfileResource(
        String fullName,
        String city,
        String country,
        String phone,
        String dni) {
}
