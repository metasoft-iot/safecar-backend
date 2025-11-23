package com.safecar.platform.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.valueobjects.Email;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Provides methods to perform CRUD operations and custom queries for users in the database.
 * </p>
 *
 * @author GonzaloQu3dena
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find
     * @return an {@link Optional} containing the found {@link User}, or empty if not found
     */
    Optional<User> findByEmail(Email email);

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check for existence
     * @return {@code true} if a user with the given email exists, {@code false} otherwise
     */
    boolean existsByEmail(Email email);
}