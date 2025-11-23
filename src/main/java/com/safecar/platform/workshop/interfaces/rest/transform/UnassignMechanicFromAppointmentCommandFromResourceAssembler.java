package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.UnassignMechanicFromAppointmentCommand;

/**
 * Assembler to convert appointment ID to UnassignMechanicFromAppointmentCommand.
 */
public class UnassignMechanicFromAppointmentCommandFromResourceAssembler {

    /**
     * Converts an appointment ID to an {@link UnassignMechanicFromAppointmentCommand}.
     *
     * @param appointmentId the appointment ID
     * @return the unassign mechanic command
     */
    public static UnassignMechanicFromAppointmentCommand toCommandFromResource(Long appointmentId) {
        return new UnassignMechanicFromAppointmentCommand(appointmentId);
    }
}
