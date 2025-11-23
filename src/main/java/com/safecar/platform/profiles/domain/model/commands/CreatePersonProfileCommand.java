package com.safecar.platform.profiles.domain.model.commands;

/**
 * Create Person Profile Command
 * <p>
 * Command to create a basic person profile with essential personal information.
 * This command only handles profile creation in the Profiles bounded context.
 * Other bounded contexts will react to PersonProfileCreatedEvent for their specific needs.
 * </p>
 * 
 * @param fullName  Full name of the person.
 * @param city      City of residence.
 * @param country   Country of residence.
 * @param phone     Contact phone number.
 * @param dni       National identification number.
 */
public record CreatePersonProfileCommand(
        String fullName,
        String city,
        String country,
        String phone,
        String dni) {
}
