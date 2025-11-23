package com.safecar.platform.shared.domain.model.events;

import java.util.Set;

/**
 * Profile Created Event
 * <p>
 * Unified event published when any type of profile (Person or Business) is created.
 * Other bounded contexts react to this event based on user roles to create 
 * their specific entities (Driver, Mechanic, Workshop, etc.).
 * </p>
 * 
 * @param profileId The unique identifier of the created profile.
 * @param userRoles The roles assigned to the user associated with the profile.
 */
public record ProfileCreatedEvent(
        Long profileId,
        Set<String> userRoles) {
}