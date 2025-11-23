package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Update Appointment Status Resource.
 * 
 * @param status The target status (PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED)
 */
public record UpdateAppointmentStatusResource(
        @NotBlank(message = "Status is required") 
        String status
) {
}