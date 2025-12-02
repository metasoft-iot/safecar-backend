package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.commands.UpdateVehicleCommand;
import com.safecar.platform.devices.interfaces.rest.resources.UpdateVehicleResource;

/**
 * Assembler to transform UpdateVehicleCommand resource to command object.
 * <p>
 * This class provides a method to perform the transformation between the
 * resource representation and the command representation.
 * </p>
 */
public class UpdateVehicleCommandFromResourceAssembler {
    /**
     * Transforms a UpdateVehicleCommand resource into a UpdateVehicleCommand.
     * 
     * @param resource the UpdateVehicleCommand resource to be transformed
     * @param id       the vehicle id
     * @return the corresponding UpdateVehicleCommand
     */
    public static UpdateVehicleCommand toCommandFromResource(UpdateVehicleResource resource, Long id) {
        return new UpdateVehicleCommand(
                id,
                resource.driverId(),
                resource.licensePlate(),
                resource.brand(),
                resource.model(),
                resource.year(),
                resource.vin(),
                resource.color(),
                resource.mileage());
    }
}