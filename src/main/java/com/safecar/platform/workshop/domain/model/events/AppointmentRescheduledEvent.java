package com.safecar.platform.workshop.domain.model.events;

import com.safecar.platform.workshop.domain.model.valueobjects.ServiceSlot;

/**
 * Event fired when an appointment is rescheduled.
 */
public record AppointmentRescheduledEvent(
        Long appointmentId,
        ServiceSlot oldSlot,
        ServiceSlot newSlot
) {
}