package com.safecar.platform.profiles.domain.services;

import java.util.Optional;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;

/**
 * Person Profile Command Service
 * <p>
 * Service interface for handling commands related to Person Profiles.
 * </p>
 */
public interface PersonProfileCommandService {
    /**
     * Handles the creation of a Person Profile.
     * 
     * @param command the {@link CreatePersonProfileCommand} containing profile details
     * @param userEmail  the email of the user for whom the profile is being created
     * @return the created Person Profile
     */
    Optional<PersonProfile> handle(CreatePersonProfileCommand command, String userEmail);
    /**
     * Handles the update of a Person Profile.
     * @param command the {@link UpdatePersonProfileCommand} containing updated profile details
     * @param personProfileId the ID of the Person Profile to update
     * @return the updated Person Profile
     */
    Optional<PersonProfile> handle(UpdatePersonProfileCommand command, Long personProfileId);
}
