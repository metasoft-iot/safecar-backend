package com.safecar.platform.workshop.domain.model.commands;

/**
 * Command to unassign a mechanic from an appointment.
 * 
 * @param appointmentId The ID of the appointment
 */
public record UnassignMechanicFromAppointmentCommand(
        Long appointmentId
) {
}
