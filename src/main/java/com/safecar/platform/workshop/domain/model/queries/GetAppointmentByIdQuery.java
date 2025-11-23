package com.safecar.platform.workshop.domain.model.queries;

/**
 * Query to get an appointment by its ID.
 */
public record GetAppointmentByIdQuery(
        Long appointmentId
) {
}

