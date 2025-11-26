package com.safecar.platform.profiles.domain.model.commands;

/**
 * Update Business Profile Command
 * <p>
 * Command object for updating a business profile with new details.
 * </p>
 */
public record UpdateBusinessProfileCommand(
        String username,
        String businessName,
        String ruc,
        String businessAddress,
        String contactPhone,
        String contactEmail,
        String description) {
}
