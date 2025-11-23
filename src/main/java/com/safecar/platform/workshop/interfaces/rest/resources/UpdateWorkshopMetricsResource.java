package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Resource for updating workshop metrics
 */
public record UpdateWorkshopMetricsResource(
        String action  // "increment_appointments", "increment_serviceorders", "complete_serviceorder"
) {
}