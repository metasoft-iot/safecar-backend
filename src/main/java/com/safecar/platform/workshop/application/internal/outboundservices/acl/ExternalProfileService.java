package com.safecar.platform.workshop.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;

/**
 * External Profile Service
 * <p>
 * This service acts as an Anti-Corruption Layer (ACL) for accessing
 * the Profiles Bounded Context from the Workshop Bounded Context.
 * It provides workshop-specific operations that require profile data.
 * </p>
 */
@Service
public class ExternalProfileService {

    private final ProfilesContextFacade profilesContextFacade;

    /**
     * Constructor
     * 
     * @param profilesContextFacade the profiles context facade
     */
    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Get business name by profile ID
     * 
     * @param profileId the business profile ID
     * @return the business name
     */
    public String getBusinessNameByProfileId(Long profileId) {
        return profilesContextFacade.getBusinessNameByProfileId(profileId);
    }

    /**
     * Get business address by profile ID
     * 
     * @param profileId the business profile ID
     * @return the business address
     */
    public String getBusinessAddressByProfileId(Long profileId) {
        return profilesContextFacade.getBusinessAddressByProfileId(profileId);
    }

    /**
     * Get business contact phone by profile ID
     * 
     * @param profileId the business profile ID
     * @return the contact phone
     */
    public String getBusinessContactPhoneByProfileId(Long profileId) {
        return profilesContextFacade.getBusinessContactPhoneByProfileId(profileId);
    }

    /**
     * Check if business profile exists by ID
     * 
     * @param profileId the business profile ID
     * @return true if exists, false otherwise
     */
    public boolean existsBusinessProfileById(Long profileId) {
        return profilesContextFacade.existsBusinessProfileById(profileId);
    }
}