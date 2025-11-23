package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Update Vehicle Resource
 * <p>
 * This record represents the data required to update an existing vehicle
 * associated with a driver.
 * It includes the driver's ID, vehicle's license plate, brand, and model.
 * </p>
 * 
 * @param driverId     the ID of the driver to whom the vehicle is associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 */
public record UpdateVehicleResource(
                Long driverId,
                String licensePlate,
                String brand,
                String model) {
}
