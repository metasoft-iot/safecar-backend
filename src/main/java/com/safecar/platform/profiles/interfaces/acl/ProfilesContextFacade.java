package com.safecar.platform.profiles.interfaces.acl;

/**
 * Anti-Corruption Layer (ACL) for Profiles Bounded Context.
 * 
 * This facade provides a stable interface for external bounded contexts
 * to interact with the Profiles domain without coupling to internal
 * implementation details.
 */
public interface ProfilesContextFacade {

        /**
         * Checks if a person profile exists for the given user email.
         *
         * @param userEmail the user email to check
         * @return true if person profile exists, false otherwise
         */
        boolean existsPersonProfileByUserEmail(String userEmail);

        /**
         * Checks if a person profile exists for the given profile ID.
         *
         * @param profileId the person profile ID to check
         * @return true if person profile exists, false otherwise
         */
        boolean existsPersonProfileById(Long profileId);

        /**
         * Retrieves the person profile ID associated with a user email.
         *
         * @param userEmail the user email
         * @return the person profile ID if found, 0 if not found
         */
        Long getPersonProfileIdByUserEmail(String userEmail);

        /**
         * Checks if a business profile exists for the given user email.
         *
         * @param userEmail the user email to check
         * @return true if business profile exists, false otherwise
         */
        boolean existsBusinessProfileByUserEmail(String userEmail);

        /**
         * Checks if a business profile exists for the given profile ID.
         *
         * @param profileId the business profile ID to check
         * @return true if business profile exists, false otherwise
         */
        boolean existsBusinessProfileById(Long profileId);

        /**
         * Retrieves the business profile ID associated with a user email.
         *
         * @param userEmail the user email
         * @return the business profile ID if found, 0 if not found
         */
        Long getBusinessProfileIdByUserEmail(String userEmail);

        /**
         * Retrieves the business name for a given business profile ID.
         *
         * @param profileId the business profile ID
         * @return the business name if found, null if not found
         */
        String getBusinessNameByProfileId(Long profileId);

        /**
         * Retrieves the business address for a given business profile ID.
         *
         * @param profileId the business profile ID
         * @return the business address if found, null if not found
         */
        String getBusinessAddressByProfileId(Long profileId);

        /**
         * Retrieves the business contact phone for a given business profile ID.
         *
         * @param profileId the business profile ID
         * @return the contact phone if found, null if not found
         */
        String getBusinessContactPhoneByProfileId(Long profileId);

        /**
         * Retrieves the full name for a given person profile ID.
         *
         * @param profileId the person profile ID
         * @return the full name if found, "Unknown" if not found
         */
        String getPersonFullNameByProfileId(Long profileId);
}
