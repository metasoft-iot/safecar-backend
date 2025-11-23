package com.safecar.platform.workshop.domain.model.events;

import java.time.Instant;

/**
 * Event fired when an appointment is canceled.
 */
public record AppointmentCanceledEvent(
        Long appointmentId,
        Instant canceledAt
) {
}