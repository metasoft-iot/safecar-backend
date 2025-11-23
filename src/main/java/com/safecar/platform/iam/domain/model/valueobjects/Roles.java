package com.safecar.platform.iam.domain.model.valueobjects;

/**
 * User roles within the system.
 * <p>
 * Defines the different roles that a user can have, which determine their permissions and access levels.
 * </p>
 * 
 * <ul>
 *  <li>{@link #ROLE_ADMIN} - Administrative user with full access.</li>
 *  <li>{@link #ROLE_CLIENT} - Basic client user.</li>
 *  <li>{@link #ROLE_DRIVER} - User with permissions related to driving functionalities.</li>
 *  <li>{@link #ROLE_MECHANIC} - User with permissions related to vehicle maintenance and repairs.</li>
 *  <li>{@link #ROLE_WORKSHOP} - Workshop owner with permissions to manage workshops and mechanics.</li>
 * </ul>
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
public enum Roles {
    ROLE_ADMIN,
    ROLE_CLIENT,
    ROLE_DRIVER,
    ROLE_MECHANIC,
    ROLE_WORKSHOP,
}
