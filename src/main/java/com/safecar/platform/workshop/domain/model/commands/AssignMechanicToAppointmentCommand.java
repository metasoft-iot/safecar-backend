package com.safecar.platform.workshop.domain.model.commands;

/**
 * Command to assign a mechanic to an appointment.
 * 
 * @param appointmentId The ID of the appointment
 * @param mechanicId The ID of the mechanic to assign
 */
public record AssignMechanicToAppointmentCommand(
        Long appointmentId,
        Long mechanicId
) {
}
