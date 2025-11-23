package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

/**
 * Reschedule Appointment Resource - Represents the raw data required to reschedule
 * an appointment. Validation will be performed when mapping to domain commands.
 * 
 * @param startAt The new start time of the appointment.
 * @param endAt   The new end time of the appointment.
 */
public record RescheduleAppointmentResource(Instant startAt, Instant endAt) {
}
