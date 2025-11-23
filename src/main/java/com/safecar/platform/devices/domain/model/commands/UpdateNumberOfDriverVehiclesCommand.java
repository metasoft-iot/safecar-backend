package com.safecar.platform.devices.domain.model.commands;

/**
 * Update Number Of Driver Vehicles Command
 * <p>
 * Command to update the number of vehicles associated with a driver.
 * </p>
 * 
 * @param driverId the unique identifier of the driver whose vehicle count is to
 *                 be updated
 */
public record UpdateNumberOfDriverVehiclesCommand(Long driverId) {
}
