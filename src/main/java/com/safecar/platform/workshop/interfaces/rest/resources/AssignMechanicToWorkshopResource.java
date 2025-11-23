package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Resource for assigning a mechanic to a workshop.
 * 
 * @param workshopId the workshop ID to which the mechanic will be assigned
 */
public record AssignMechanicToWorkshopResource(
        @NotNull(message = "Workshop ID is required")
        Long workshopId) {
}
