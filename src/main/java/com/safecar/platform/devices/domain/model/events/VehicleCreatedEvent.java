package com.safecar.platform.devices.domain.model.events;

/**
 * Vehicle Created Event
 * <p>
 * Event triggered when a new vehicle is created in the system.
 * </p>
 * 
 * @param vehicleId the unique identifier of the created vehicle
 * @param driverId the unique identifier of the driver who owns the vehicle
 */
public record VehicleCreatedEvent(Long vehicleId, Long driverId) {
}
