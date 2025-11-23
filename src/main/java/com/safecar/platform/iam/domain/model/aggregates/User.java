package com.safecar.platform.iam.domain.model.aggregates;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.safecar.platform.iam.domain.model.commands.SignUpCommand;
import com.safecar.platform.iam.domain.model.entities.Role;
import com.safecar.platform.iam.domain.model.valueobjects.Email;
import com.safecar.platform.iam.domain.model.valueobjects.Password;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.AttributeOverride;

/**
 * User Aggregate
 * <p>
 * Represents a user in the system with email, password, and roles.
 * </p>
 * 
 * @author GonzaloQu3dena
 * @version 1.0
 * @since 2025-10-05
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class User extends AuditableAbstractAggregateRoot<User> {

    /**
     * User's email address
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;

    /**
     * User's password
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;

    /**
     * Roles assigned to the user
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Default constructor initializing the user with a default role.
     */
    public User() {
        // No-op: do not add a transient default Role here. Role assignment
        // should be handled by the application/service layer where the
        // persisted Role entity can be fetched and assigned.
    }

    /**
     * Parameterized constructor to create a user with specified email, password,
     * and role.
     * 
     * @param email    User's email address
     * @param password User's password
     * @param role     Role to be assigned to the user
     */
    public User(String email, String password, Role role) {
        this.email = new Email(email);
        this.password = new Password(password);
        this.roles.add(role);
    }

    public User(String email, String password, Set<Role> roles) {
        this.email = new Email(email);
        this.password = new Password(password);
        this.addRoles(roles);
    }

    /**
     * Get the user's email address.
     * 
     * @return User's email address
     */
    public String getEmail() {
        return this.email.value();
    }

    /**
     * Get the user's password.
     * 
     * @return User's password
     */
    public String getPassword() {
        return this.password.value();
    }

    /**
     * Get the names of roles assigned to the user.
     * 
     * @return Set of role names
     */
    public Set<String> getRoleNames() {
        return this.roles.stream()
                .map(Role::getStringName)
                .collect(Collectors.toSet());
    }

    /**
     * Check if the user has a specific role.
     * 
     * @param roleName Name of the role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String roleName) {
        return this.roles.stream()
                .anyMatch(role -> role.getStringName().equals(roleName));
    }

    /**
     * Add a set of roles to the user.
     *
     * @param roles the roles.
     * @return the user.
     */
    public User addRoles(Set<Role> roles) {
        var validatedRoles = Role.validateRoles(roles);
        this.roles.addAll(validatedRoles);
        return this;
    }

    /**
     * Get roles as a set of strings.
     * 
     * @return Set of role names as strings
     */
    public Set<String> getRolesAsStringSet() {
        return this.roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

    /**
     * Factory method to create a User from a SignUpCommand and a Role.
     * 
     * @param command SignUpCommand containing user details
     * @param role    Role to be assigned to the new user
     * @return Newly created User instance
     */
    public static User create(SignUpCommand command, Role role) {
        return new User(command.email(), command.password(), role);
    }
}
