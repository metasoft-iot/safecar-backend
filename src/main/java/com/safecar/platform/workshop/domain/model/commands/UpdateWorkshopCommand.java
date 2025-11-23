package com.safecar.platform.workshop.domain.model.commands;

/**
 * Update Workshop Command
 * Carries data to update workshop attributes (currently only description).
 *
 * @param workshopId         the workshop id
 * @param workshopDescription new description text
 */
public record UpdateWorkshopCommand(Long workshopId, String workshopDescription) {
}
