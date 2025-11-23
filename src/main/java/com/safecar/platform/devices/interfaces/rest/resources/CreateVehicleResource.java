package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Create Vehicle Resource
 * <p>
 * This record represents the raw data required to create a new vehicle associated
 * with a driver. Validation will be performed when mapping to domain commands and value objects.
 * </p>
 * 
 * @param driverId     the ID of the driver to whom the vehicle will be associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 */
public record CreateVehicleResource(
        Long driverId,
        String licensePlate,
        String brand,
        String model
) {
}
