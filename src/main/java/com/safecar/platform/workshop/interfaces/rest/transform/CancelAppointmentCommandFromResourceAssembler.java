package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.CancelAppointmentCommand;
import com.safecar.platform.workshop.interfaces.rest.resources.CancelAppointmentResource;

/**
 * Cancel Appointment Command From Resource Assembler - Converts
 * CancelAppointmentResource to CancelAppointmentCommand.
 */
public class CancelAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link CancelAppointmentResource} to a
     * {@link CancelAppointmentCommand}.
     *
     * @param appointmentId the appointment ID
     * @param resource      the cancel appointment resource
     * @return the cancel appointment command
     */
    public static CancelAppointmentCommand toCommandFromResource(Long appointmentId,
            CancelAppointmentResource resource) {
        return new CancelAppointmentCommand(appointmentId);
    }
}
