package com.safecar.platform.iam.infrastructure.authorization.sfs.services;

import com.safecar.platform.iam.domain.exceptions.EmailNotFoundException;
import com.safecar.platform.iam.domain.model.valueobjects.Email;
import com.safecar.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User Details Service Implementation
 * <p>
 * This service is responsible for loading user-specific data during the
 * authentication process.
 * </p>
 */
@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements ExtendedUserDetailsService {

    // {@inheritDoc}
    private final UserRepository userRepository;

    /**
     * Constructor for UserDetailsServiceImpl.
     * 
     * @param userRepository the user repository to be used for fetching user data.
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load User by Username
     * <p>
     * This method is overridden to throw an UnsupportedOperationException,
     * as loading by username is not supported in this implementation.
     * </p>
     * 
     * @param username the username of the user to be loaded.
     * @return UserDetails object containing user information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Loading user by username is not supported. Use loadUserByEmail instead."); 
    }

    /**
     * Load User by Email
     * <p>
     * This method retrieves user details based on the provided email address.
     * </p>
     * 
     * @param email the email of the user to be loaded.
     * @return UserDetails object containing user information.
     * @throws EmailNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByEmail(String email) throws EmailNotFoundException {
        var user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> new EmailNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }
}