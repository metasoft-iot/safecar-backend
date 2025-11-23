package com.safecar.platform.workshop.domain.exceptions;

/**
 * Exception thrown when an appointment is not found.
 */
public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(Long id) {
        super("Appointment not found with id: " + id);
    }

    public AppointmentNotFoundException(String code) {
        super("Appointment not found with code: " + code);
    }
}

