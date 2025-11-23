package com.safecar.platform.workshop.domain.model.events;

/**
 * Event fired when a mechanic is assigned to an appointment.
 * 
 * @param appointmentId The ID of the appointment
 * @param mechanicId The ID of the mechanic assigned
 */
public record MechanicAssignedToAppointmentEvent(
        Long appointmentId,
        Long mechanicId
) {
}
