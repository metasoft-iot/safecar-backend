package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Create Vehicle Resource
 * <p>
 * This record represents the raw data required to create a new vehicle
 * associated
 * with a driver. Validation will be performed when mapping to domain commands
 * and value objects.
 * </p>
 * 
 * @param driverId     the ID of the driver to whom the vehicle will be
 *                     associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 * @param year         the manufacturing year of the vehicle (optional)
 * @param vin          the Vehicle Identification Number (optional)
 * @param color        the color of the vehicle (optional)
 * @param mileage      the current mileage of the vehicle (optional)
 */
public record CreateVehicleResource(
                Long driverId,
                String licensePlate,
                String brand,
                String model,
                Integer year,
                String vin,
                String color,
                Integer mileage) {
}
