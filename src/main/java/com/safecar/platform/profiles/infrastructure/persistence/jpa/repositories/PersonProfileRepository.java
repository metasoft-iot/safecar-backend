package com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Person Profile Repository JPA
 * <p>
 * JPA Repository interface for managing PersonProfile entities.
 * </p>
 */
@Repository
public interface PersonProfileRepository extends JpaRepository<PersonProfile, Long> {
    /**
     * Finds a PersonProfile by the associated user email.
     * 
     * @param userEmail the user email
     * @return an Optional containing the found PersonProfile, or empty if not found
     */
    Optional<PersonProfile> findByUserEmail(String userEmail);

    /**
     * Checks if a PersonProfile exists for the given user email.
     * 
     * @param userEmail the user email
     * @return true if a PersonProfile exists, false otherwise
     */
    boolean existsByUserEmail(String userEmail);

    /**
     * Checks if a PersonProfile exists with the given DNI.
     * 
     * @param dni the DNI
     * @return true if a PersonProfile exists, false otherwise
     */
    boolean existsByDni_Dni(String dni);

    /**
     * Checks if a PersonProfile exists with the given phone number.
     * 
     * @param phone the phone number
     * @return true if a PersonProfile exists, false otherwise
     */
    boolean existsByPhone_Phone(String phone);
}
