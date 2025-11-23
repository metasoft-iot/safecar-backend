package com.safecar.platform.devices.interfaces.rest.resources;

/**
 * Update Driver Resource
 * 
 * <p>
 * Represents the resource for updating driver information.
 * </p>
 * 
 * @param driverId the ID of the driver to be updated
 */
public record UpdateDriverMetricsResource(Long driverId) {
}
