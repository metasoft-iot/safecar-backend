package com.safecar.platform.workshop.domain.model.valueobjects;

/**
 * Mechanic specializations within the system.
 * <p>
 * Defines the different specializations that a mechanic can have, which determine their expertise areas.
 * </p>
 * 
 * <ul>
 *  <li>{@link #ENGINE} - Engine maintenance and repair specialist.</li>
 *  <li>{@link #BRAKES} - Brake system specialist.</li>
 *  <li>{@link #TRANSMISSION} - Transmission system specialist.</li>
 *  <li>{@link #ELECTRICAL} - Electrical system specialist.</li>
 *  <li>{@link #SUSPENSION} - Suspension system specialist.</li>
 *  <li>{@link #AIR_CONDITIONING} - Air conditioning system specialist.</li>
 *  <li>{@link #BODYWORK} - Bodywork and paint specialist.</li>
 *  <li>{@link #DIAGNOSTICS} - Vehicle diagnostics specialist.</li>
 * </ul>

 
 */
public enum Specializations {
    GENERAL,
    ENGINE,
    BRAKES,
    TRANSMISSION,
    ELECTRICAL,
    SUSPENSION,
    AIR_CONDITIONING,
    BODYWORK,
    DIAGNOSTICS
}