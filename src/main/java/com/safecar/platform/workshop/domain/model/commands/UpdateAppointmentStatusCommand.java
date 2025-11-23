package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;

/**
 * Command to update appointment status.
 */
public record UpdateAppointmentStatusCommand(
        Long appointmentId,
        AppointmentStatus status
) {
}