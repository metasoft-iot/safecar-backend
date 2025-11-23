package com.safecar.platform.workshop.domain.exceptions;

/**
 * Exception thrown when an invalid appointment state transition is attempted.
 */
public class InvalidAppointmentStateTransitionException extends RuntimeException {

    public InvalidAppointmentStateTransitionException(String message) {
        super(message);
    }

    public InvalidAppointmentStateTransitionException(String currentState, String targetState) {
        super("Cannot transition from " + currentState + " to " + targetState);
    }
}

