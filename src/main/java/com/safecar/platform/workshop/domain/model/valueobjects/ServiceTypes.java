package com.safecar.platform.workshop.domain.model.valueobjects;

/**
 * ServiceTypes
 * <p>
 *     This enum represents the types of services that can be requested for a vehicle appointment.
 * </p>
 */
public enum ServiceTypes {
    /**
     * Oil change and filter replacement service
     */
    OIL_CHANGE,
    
    /**
     * Brake inspection, pad replacement, and brake system service
     */
    BRAKE_SERVICE,
    
    /**
     * Tire rotation, replacement, and wheel alignment service
     */
    TIRE_SERVICE,
    
    /**
     * Engine diagnostics and troubleshooting service
     */
    ENGINE_DIAGNOSTICS,
    
    /**
     * Transmission repair and maintenance service
     */
    TRANSMISSION_SERVICE,
    
    /**
     * Electrical system diagnostics and repair service
     */
    ELECTRICAL_REPAIR,
    
    /**
     * Air conditioning system service and repair
     */
    AIR_CONDITIONING,
    
    /**
     * Suspension system inspection and repair service
     */
    SUSPENSION_REPAIR,
    
    /**
     * General maintenance and inspection service
     */
    GENERAL_MAINTENANCE,
    
    /**
     * Custom service type (requires custom description)
     */
    CUSTOM
}
