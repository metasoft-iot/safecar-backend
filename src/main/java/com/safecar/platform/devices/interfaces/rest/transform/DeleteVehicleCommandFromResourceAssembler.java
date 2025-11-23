package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.commands.DeleteVehicleCommand;

/**
 * Assembler to transform DeleteVehicleCommand resource to command
 * <p>
 * This class provides a static method to perform the transformation between the
 * resource representation and the command representation.
 * </p>
 */
public class DeleteVehicleCommandFromResourceAssembler {
    /**
     * Transforms a DeleteVehicleCommand resource into a DeleteVehicleCommand.
     * 
     * @param resource the DeleteVehicleCommand resource to be transformed
     * @param id       the vehicle id
     * @return the corresponding DeleteVehicleCommand
     */
    public static DeleteVehicleCommand toCommandFromResource(DeleteVehicleCommand resource, Long id) {
        return new DeleteVehicleCommand(
                id,
                resource.driverId()

        );
    }
}
