package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.workshop.domain.model.valueobjects.Specializations;

/**
 * Repository interface for managing {@link Specialization} entities.
 * <p>
 * Provides methods to perform CRUD operations and custom queries for specializations in the database.
 * </p>
 *
 * @author SafeCar Platform Team
 * @version 1.0
 * @since 2025-11-07
 */
@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    /**
     * Finds a specialization by its name.
     *
     * @param name the {@link Specializations} value object representing the specialization name
     * @return an {@link Optional} containing the found {@link Specialization}, or empty if not found
     */
    Optional<Specialization> findByName(Specializations name);

    /**
     * Checks if a specialization exists by its name.
     *
     * @param name the {@link Specializations} value object representing the specialization name
     * @return {@code true} if a specialization with the given name exists, {@code false} otherwise
     */
    boolean existsByName(Specializations name);
}