package com.safecar.platform.workshop.domain.model.queries;

import java.time.Instant;

import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

/**
 * Query to get appointments by workshop and time range.
 */
public record GetAppointmentsByWorkshopAndRangeQuery(
        WorkshopId workshopId,
        Instant from,
        Instant to
) {
}