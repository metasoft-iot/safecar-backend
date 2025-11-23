package com.safecar.platform.profiles.domain.services;

import java.util.Optional;

import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdateBusinessProfileCommand;

/**
 * Business Profile Command Service
 */
public interface BusinessProfileCommandService {

    /**
     * Handle the creation of a new BusinessProfile.
     * 
     * @param command   the {@link CreateBusinessProfileCommand} instance
     * @param userEmail the user email to associate with the profile
     * @return the created {@link BusinessProfile} instance
     */
    Optional<BusinessProfile> handle(CreateBusinessProfileCommand command, String userEmail);

    /**
     * Handle the update of an existing BusinessProfile.
     * @param command the {@link UpdateBusinessProfileCommand} instance
     * @return the updated {@link BusinessProfile} instance
     */
    Optional<BusinessProfile> handle(UpdateBusinessProfileCommand command, Long businessProfileId);
}