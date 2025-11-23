package com.safecar.platform.workshop.domain.model.events;

import com.safecar.platform.workshop.domain.model.valueobjects.*;

/**
 * Event fired when an appointment is created.
 */
public record AppointmentCreatedEvent(
        Long appointmentId,
        WorkshopId workshopId,
        VehicleId vehicleId,
        DriverId driverId,
        ServiceSlot slot,
        Long workOrderId // Optional, can be null
) {
}