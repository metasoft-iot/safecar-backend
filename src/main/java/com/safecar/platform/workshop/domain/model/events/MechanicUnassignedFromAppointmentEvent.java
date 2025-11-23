package com.safecar.platform.workshop.domain.model.events;

/**
 * Event fired when a mechanic is unassigned from an appointment.
 * 
 * @param appointmentId The ID of the appointment
 * @param previousMechanicId The ID of the mechanic that was unassigned
 */
public record MechanicUnassignedFromAppointmentEvent(
        Long appointmentId,
        Long previousMechanicId
) {
}
