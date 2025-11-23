package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.UpdateAppointmentStatusCommand;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateAppointmentStatusResource;

/**
 * Assembler to convert UpdateAppointmentStatusResource to
 * UpdateAppointmentStatusCommand.
 */
public class UpdateAppointmentStatusCommandFromResourceAssembler {

    /**
     * Converts resource to command, validating the status string.
     * 
     * @param appointmentId the appointment ID from path
     * @param resource      the status update resource
     * @return the update appointment status command
     * @throws IllegalArgumentException if status is invalid
     */
    public static UpdateAppointmentStatusCommand toCommandFromResource(Long appointmentId,
            UpdateAppointmentStatusResource resource) {

        var status = AppointmentStatusEnumValueFromSetAssembler
                .toAppointmentStatusEnumValueFromString(resource.status());

        return new UpdateAppointmentStatusCommand(appointmentId, status);
    }
}