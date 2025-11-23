package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Workshop Repository.
 * <p>
 * This interface is used to interact with the database to perform CRUD operations on {@link Workshop} aggregates.
 * It extends {@link JpaRepository} to provide basic CRUD operations and defines additional query methods.
 * </p>
 */
@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    
    /**
     * Check if a Workshop exists by business profile ID.
     *
     * @param businessProfileId the business profile ID to check
     * @return true if the Workshop exists, false otherwise
     */
    boolean existsByBusinessProfileId(ProfileId businessProfileId);

    /**
     * Find a Workshop by business profile ID.
     *
     * @param businessProfileId the business profile ID to search for
     * @return an {@link Optional} containing the Workshop if found
     */
    Optional<Workshop> findByBusinessProfileId(ProfileId businessProfileId);
}