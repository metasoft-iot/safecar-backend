package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Cancel Appointment Resource - Represents the data required to cancel an
 * appointment.
 * 
 * @param reason The reason for cancellation (optional).
 */
public record CancelAppointmentResource(
                String reason) {
}
