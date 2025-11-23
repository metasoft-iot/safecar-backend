package com.safecar.platform.iam.interfaces.acl;

import java.util.Set;

/**
 * IamContextFacade
 * <p>
 *     This interface provides the methods to interact with the IAM context.
 *     It provides the methods to create a user, fetch user information by email/userId.
 *     The implementation of this interface will be provided by the IAM module.
 *     This interface is used by other bounded contexts to interact with the IAM module.
 * </p>
 */
public interface IamContextFacade {

    /**
     * fetchUserIdByEmail
     * <p>
     *     This method is used to fetch the userId by email.
     * </p>
     * @param email the email of the user
     * @return the user id of the user if found, 0L otherwise
     */
    Long fetchUserIdByEmail(String email);

    /**
     * fetchUserEmailByUserId
     * <p>
     *     This method is used to fetch the user email by userId.
     * </p>
     * @param userId the userId of the user
     * @return the email of the user if found, empty string otherwise
     */
    String fetchUserEmailByUserId(Long userId);

    /**
     * validateUserExists
     * <p>
     *     This method validates if a user exists by userId.
     * </p>
     * @param userId the userId to validate
     * @return true if user exists, false otherwise
     */
    boolean validateUserExists(Long userId);

    /**
     * validateUserExistsByEmail
     * <p>
     *     This method validates if a user exists by email.
     * </p>
     * @param email the email to validate
     * @return true if user exists, false otherwise
     */
    boolean validateUserExistsByEmail(String email);

    /**
     * fetchUserRolesByUserId
     * <p>
     *     This method is used to fetch the user roles by userId.
     * </p>
     * @param userId the userId of the user
     * @return the set of role names (as strings) of the user if found, empty set otherwise
     */
    Set<String> fetchUserRolesByUserId(Long userId);
}
