package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Update Vehicle Resource
 * <p>
 * This record represents the data required to update an existing vehicle
 * associated with a driver.
 * It includes the driver's ID, vehicle's license plate, brand, model, year,
 * VIN, color, and mileage.
 * </p>
 * 
 * @param driverId     the ID of the driver to whom the vehicle is associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 * @param year         the manufacturing year of the vehicle (optional)
 * @param vin          the Vehicle Identification Number (optional)
 * @param color        the color of the vehicle (optional)
 * @param mileage      the current mileage of the vehicle (optional)
 */
public record UpdateVehicleResource(
        Long driverId,
        String licensePlate,
        String brand,
        String model,
        Integer year,
        String vin,
        String color,
        Integer mileage) {
}
