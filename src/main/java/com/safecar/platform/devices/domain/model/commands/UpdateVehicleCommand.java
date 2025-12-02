package com.safecar.platform.devices.domain.model.commands;

/**
 * Update Vehicle Command - Command to update an existing vehicle's details.
 *
 * @param vehicleId    the id of the vehicle to be updated
 * @param driverId     the id of the driver associated with the vehicle
 * @param licensePlate the license plate of the vehicle
 * @param brand        the brand of the vehicle
 * @param model        the model of the vehicle
 * @param year         the manufacturing year of the vehicle (optional)
 * @param vin          the Vehicle Identification Number (optional)
 * @param color        the color of the vehicle (optional)
 * @param mileage      the current mileage of the vehicle (optional)
 */
public record UpdateVehicleCommand(
        Long vehicleId,
        Long driverId,
        String licensePlate,
        String brand,
        String model,
        Integer year,
        String vin,
        String color,
        Integer mileage) {
    public UpdateVehicleCommand {
        if (vehicleId == null)
            throw new IllegalArgumentException("Vehicle ID must be a positive non-null value.");
        if (driverId == null || driverId <= 0)
            throw new IllegalArgumentException("Driver ID must be a positive non-null value.");
        if (licensePlate == null || licensePlate.isBlank())
            throw new IllegalArgumentException("License plate must be a non-null, non-blank value.");
        if (brand == null || brand.isBlank())
            throw new IllegalArgumentException("Brand must be a non-null, non-blank value.");
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("Model must be a non-null, non-blank value.");
    }
}
