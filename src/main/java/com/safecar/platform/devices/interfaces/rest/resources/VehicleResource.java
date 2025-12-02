package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Vechicle Resource
 * <p>
 * Represents a vehicle with its associated details. Includes the vehicle's ID,
 * driver's ID, license plate, brand, model, year, VIN, color, and mileage.
 * </p>
 * 
 * @param id           the unique identifier of the vehicle
 * @param driverId     the ID of the driver to whom the vehicle is associated
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 * @param year         the manufacturing year of the vehicle
 * @param vin          the Vehicle Identification Number
 * @param color        the color of the vehicle
 * @param mileage      the current mileage of the vehicle
 */
public record VehicleResource(
        Long id,
        Long driverId,
        String licensePlate,
        String brand,
        String model,
        Integer year,
        String vin,
        String color,
        Integer mileage) {
}
