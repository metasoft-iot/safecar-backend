package com.safecar.platform.workshop.domain.model.commands;

/**
 * Command to assign a mechanic to a workshop.
 * 
 * @param mechanicId the mechanic ID to assign
 * @param workshopId the workshop ID
 */
public record AssignMechanicToWorkshopCommand(
        Long mechanicId,
        Long workshopId) {
}
