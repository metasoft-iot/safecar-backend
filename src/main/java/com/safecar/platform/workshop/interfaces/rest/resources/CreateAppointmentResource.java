package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;

/**
 * Create Appointment Resource - Represents the raw data required to create a new
 * appointment. Validation will be performed when mapping to domain commands.
 * 
 * @param workshopId The ID of the workshop where the appointment is to be
 *                   created.
 * @param vehicleId  The ID of the vehicle for which the appointment is being
 *                   made.
 * @param driverId   The ID of the driver associated with the appointment.
 * @param startAt    The start time of the appointment.
 * @param endAt      The end time of the appointment.
 * @param serviceType The type of service requested (e.g., "OIL_CHANGE", "BRAKE_SERVICE").
 * @param customServiceDescription Optional description when serviceType is "CUSTOM".
 */
public record CreateAppointmentResource(
        Long workshopId,
        Long vehicleId,
        Long driverId,
        Instant startAt,
        Instant endAt,
        String serviceType,
        String customServiceDescription) {
}
