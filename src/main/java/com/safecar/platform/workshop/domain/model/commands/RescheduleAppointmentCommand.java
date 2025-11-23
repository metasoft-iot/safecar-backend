package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceSlot;

/**
 * Command to reschedule an appointment.
 */
public record RescheduleAppointmentCommand(
        Long appointmentId,
        ServiceSlot slot
) {
}

