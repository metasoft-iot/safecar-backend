package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshop.domain.model.entities.ServiceType;
import com.safecar.platform.workshop.domain.model.valueobjects.*;
import com.safecar.platform.workshop.interfaces.rest.resources.CreateAppointmentResource;

/**
 * Create Appointment Command From Resource Assembler - Converts
 * CreateAppointmentResource to CreateAppointmentCommand.
 */
public class CreateAppointmentCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateAppointmentResource} to a
     * {@link CreateAppointmentCommand}.
     *
     * @param resource the create appointment resource
     * @return the create appointment command
     */
    public static CreateAppointmentCommand toCommandFromResource(CreateAppointmentResource resource) {
        var workshopId = new WorkshopId(resource.workshopId());
        var vehicleId = new VehicleId(resource.vehicleId());
        var driverId = new DriverId(resource.driverId());
        var slot = new ServiceSlot(resource.startAt(), resource.endAt());
        
        // Convert string service type to ServiceType entity
        ServiceType serviceType = null;
        if (resource.serviceType() != null && !resource.serviceType().isBlank()) {
            serviceType = ServiceType.toServiceTypeFromName(resource.serviceType());
        }

        return new CreateAppointmentCommand(
                workshopId,
                vehicleId,
                driverId,
                slot,
                serviceType,
                resource.customServiceDescription());
    }
}
