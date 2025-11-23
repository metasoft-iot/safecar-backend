package com.safecar.platform.devices.domain.model.commands;

/**
 * Delete Vehicle Command - Command to delete a vehicle from the system.
 * 
 * @param vehicleId the id of the vehicle to be deleted
 * @param driverId  the id of the driver associated with the vehicle
 */
public record DeleteVehicleCommand(
        Long vehicleId,
        Long driverId) {
            
    public DeleteVehicleCommand {
        if (vehicleId == null || vehicleId <= 0)
            throw new IllegalArgumentException("Vehicle ID must be a positive non-null value.");
    }
}
