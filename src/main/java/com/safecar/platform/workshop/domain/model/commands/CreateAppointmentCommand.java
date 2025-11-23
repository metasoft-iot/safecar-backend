package com.safecar.platform.workshop.domain.model.commands;

import com.safecar.platform.workshop.domain.model.entities.ServiceType;
import com.safecar.platform.workshop.domain.model.valueobjects.*;

/**
 * Command to create a new workshop appointment.
 * 
 * @param workshopId The workshop ID where the appointment is scheduled
 * @param vehicleId The vehicle ID for the appointment
 * @param driverId The driver ID who owns the vehicle
 * @param slot The scheduled time slot for the appointment
 * @param serviceType The type of service requested (following IAM Role pattern)
 * @param customServiceDescription Optional description when serviceType is CUSTOM
 */
public record CreateAppointmentCommand(
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        ServiceSlot slot,
        ServiceType serviceType,
        String customServiceDescription
) {
}

