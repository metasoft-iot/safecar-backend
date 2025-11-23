package com.safecar.platform.workshop.domain.model.commands;

/**
 * Create Workshop Command
 * <p>
 * Workshop name, address, and phone are obtained from the associated BusinessProfile.
 * Only workshop-specific information is included in this command.
 * </p>
 * 
 * @param businessProfileId   the business profile ID that owns the workshop
 * @param workshopDescription the workshop description (optional)
 */
public record CreateWorkshopCommand(
        Long businessProfileId,
        String workshopDescription
) {
}