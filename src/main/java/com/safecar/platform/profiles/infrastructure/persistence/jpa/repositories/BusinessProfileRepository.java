package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Business Profile Repository
 */
@Repository
public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {

    /**
     * Find business profile by user email
     * 
     * @param userEmail the user email
     * @return an optional business profile
     */
    Optional<BusinessProfile> findByUserEmail(String userEmail);

    /**
     * Check if business profile exists by user email
     * 
     * @param userEmail the user email
     * @return true if exists, false otherwise
     */
    boolean existsByUserEmail(String userEmail);
}