package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Update Appointment Information Resource - Represents the raw data required to
 * update an appointment's information. Validation will be performed when mapping to domain commands.
 * 
 * @param serviceType The type of service for the appointment.
 * @param description Additional description or notes for the appointment.
 */
public record UpdateAppointmentInformationResource(String serviceType, String description) {
}
