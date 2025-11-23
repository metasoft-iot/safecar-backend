package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.interfaces.rest.resources.CreateVehicleResource;

/**
 * Assembler to transform CreateVehicleResource to CreateVehicleCommand.
 * <p>
 * This class provides a static method to perform the transformation between the
 * resource representation
 * and the command representation.
 * </p>
 */
public class CreateVehicleCommandFromVehicleResourceAssembler {
    /**
     * Transforms a CreateVehicleResource into a CreateVehicleCommand.
     * 
     * @param resource the CreateVehicleResource to be transformed
     * @return the corresponding CreateVehicleCommand
     */
    public static CreateVehicleCommand toCommandFromVehicleResource(CreateVehicleResource resource) {
        return new CreateVehicleCommand(
                resource.driverId(),
                resource.licensePlate(),
                resource.brand(),
                resource.model());
    }
}
