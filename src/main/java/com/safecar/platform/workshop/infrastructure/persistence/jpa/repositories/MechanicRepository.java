package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Mechanic Repository - JPA Repository for Mechanic entities.
 */
@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    /**
     * Finds a Mechanic by their associated profile ID.
     * 
     * @param profileId the profile ID to search for
     * @return an Optional containing the found Mechanic, or empty if not found
     */
    Optional<Mechanic> findByProfileId_ProfileId(Long profileId);

    /**
     * Checks if a Mechanic exists by their associated profile ID.
     * 
     * @param profileId the profile ID to check for
     * @return true if the Mechanic exists, false otherwise
     */
    boolean existsByProfileId_ProfileId(Long profileId);

    /**
     * Finds Mechanics by their associated workshop ID.
     * 
     * @param workshopId the workshop ID to search for
     * @return a List of found Mechanics
     */
    java.util.List<Mechanic> findByWorkshopId(Long workshopId);

}