package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Driver Resource
 * <p>
 * Represents the resource for driver information.
 * </p>
 * 
 * @param driverId      the ID of the driver
 * @param totalVehicles the total number of vehicles associated with the driver
 */
public record DriverResource(
                Long driverId,
                Integer totalVehicles) {

}
