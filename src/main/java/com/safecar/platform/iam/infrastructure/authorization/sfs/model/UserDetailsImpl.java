package com.safecar.platform.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safecar.platform.iam.domain.model.aggregates.User;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

/**
 * Implementation of {@link UserDetails} for Spring Security.
 * <p>
 * Encapsulates user information such as username, password, authorities, and account status flags.
 * Used by Spring Security for authentication and authorization.
 * </p>
 */
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    /**
     * The username identifying the user.
     */
    private final String username;

    /**
     * The password used to authenticate the user.
     * This field is ignored during JSON serialization.
     */
    @JsonIgnore
    private final String password;

    /**
     * Indicates whether the user is enabled.
     */
    private final boolean enabled;

    /**
     * Indicates whether the user's account is not locked.
     */
    private final boolean accountNonLocked;

    /**
     * Indicates whether the user's account is not expired.
     */
    private final boolean accountNonExpired;

    /**
     * Indicates whether the user's credentials are not expired.
     */
    private final boolean credentialsNonExpired;

    private final Long id;

    /**
     * The authorities granted to the user.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a new {@code UserDetailsImpl} with the provided username, password, and authorities.
     * All account status flags are set to {@code true} by default.
     *
     * @param username    the username identifying the user
     * @param password    the password for the user
     * @param authorities the authorities granted to the user
     */
    public UserDetailsImpl(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }


       /**
     * Builds a {@code UserDetailsImpl} instance from the supplied {@code User}.
     *
     * <p>
     * Extracts all the user's roles and converts them to {@code SimpleGrantedAuthority} to set up the user's authorities.
     * </p>
     *
     * @param user the user aggregate containing user details
     * @return a new {@code UserDetailsImpl} instance
     */
    public static UserDetailsImpl build(User user) {
        var authorities = user.getRoleNames().stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
        
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}