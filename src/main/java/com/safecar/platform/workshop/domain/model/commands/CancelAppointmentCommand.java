package com.safecar.platform.workshop.domain.model.commands;

/**
 * Command to cancel an appointment.
 */
public record CancelAppointmentCommand(
        Long appointmentId
) {
}

