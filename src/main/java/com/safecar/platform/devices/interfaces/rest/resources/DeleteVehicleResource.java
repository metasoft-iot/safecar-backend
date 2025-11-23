package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Delete Vehicle Resource
 * <p>
 * Represents the resource for deleting a vehicle. It contains the vehicle ID to
 * be deleted.
 * </p>
 * 
 * @param vehicleId The ID of the vehicle to be deleted.
 */
public record DeleteVehicleResource(
                Long vehicleId) {
}
