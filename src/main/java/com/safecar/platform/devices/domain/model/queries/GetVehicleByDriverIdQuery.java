package com.safecar.platform.devices.domain.model.queries;

/**
 * Get Vehicle by Driver ID Query - Query to get a vehicle by driver id.
 *
 * @param driverId the id of the driver
 */
public record GetVehicleByDriverIdQuery(Long driverId) {
}
