package com.safecar.platform.iam.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByIdQuery;

/**
 * Service interface for handling user-related query operations.
 * <p>
 * Provides methods to retrieve users based on different query criteria.
 *
 * @author GonzaloQu3dena
 * @since 2025-10-06
 * @version 1.0.0
 */
public interface UserQueryService {

    /**
     * Handles the query to retrieve all users.
     *
     * @param query the query object for retrieving all users
     * @return a list of all {@link UserAggregate} instances
     */
    List<User> handle(GetAllUsersQuery query);

    /**
     * Handles the query to retrieve a user by their email.
     *
     * @param query the query object containing the email
     * @return an {@link Optional} containing the found {@link User}, or empty if not found
     */
    Optional<User> handle(GetUserByEmailQuery query);

    /**
     * Handles the query to retrieve a user by their ID.
     *
     * @param query the query object containing the user ID
     * @return an {@link Optional} containing the found {@link User}, or empty if not found
     */
    Optional<User> handle(GetUserByIdQuery query);
}