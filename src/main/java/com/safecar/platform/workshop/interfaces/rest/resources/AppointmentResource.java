package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

/**
 * Appointment Resource - Represents an appointment in the workshop.
 * 
 * @param id          the unique identifier of the appointment
 * @param workshopId  the ID of the workshop where the appointment is scheduled
 * @param vehicleId   the ID of the vehicle associated with the appointment
 * @param driverId    the ID of the driver associated with the appointment
 * @param startAt     the start time of the appointment
 * @param endAt       the end time of the appointment
 * @param status      the current status of the appointment
 * @param serviceType the type of service requested (e.g., "OIL_CHANGE", "BRAKE_SERVICE")
 * @param customServiceDescription optional description when serviceType is "CUSTOM"
 * @param mechanicId  the ID of the mechanic assigned to this appointment (optional)
 * @param notes       the list of notes associated with the appointment
 */
public record AppointmentResource(
                Long id,
                Long workshopId,
                Long vehicleId,
                Long driverId,
                Instant startAt,
                Instant endAt,
                String status,
                String serviceType,
                String customServiceDescription,
                Long mechanicId,
                List<AppointmentNoteResource> notes) {
}
