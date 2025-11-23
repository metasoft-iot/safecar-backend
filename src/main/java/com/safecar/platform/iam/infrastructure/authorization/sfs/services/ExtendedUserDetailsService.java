package com.safecar.platform.iam.infrastructure.authorization.sfs.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.safecar.platform.iam.domain.exceptions.EmailNotFoundException;

/**
 * Extended User Details Service interface
 * <p>
 * Extends the standard UserDetailsService to include loading user details by
 * email.
 * </p>
 */
public interface ExtendedUserDetailsService extends UserDetailsService {
    /**
     * Load UserDetails by email
     * <p>
     * This method retrieves user details based on the provided email address.
     * </p>
     * @param email the email address of the user
     * @return UserDetails corresponding to the email
     * @throws EmailNotFoundException if no user is found with the given email
     */
    UserDetails loadUserByEmail(String email) throws EmailNotFoundException;
}
