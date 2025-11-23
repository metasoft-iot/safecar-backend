package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Vechicle Resource
 * <p>
 * Represents a vehicle with its associated details. Includes the vehicle's ID,
 * driver's ID, license plate,
 * brand, and model.
 * </p>
 * 
 * @param id           the unique identifier of the vehicle
 * @param driverId     the ID of the driver to whom the vehicle is associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 */
public record VehicleResource(
                Long id,
                Long driverId,
                String licensePlate,
                String brand,
                String model

) {
}
