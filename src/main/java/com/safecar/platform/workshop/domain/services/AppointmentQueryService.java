package com.safecar.platform.workshop.domain.services;

import java.util.List;
import java.util.Optional;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.queries.*;

/**
 * Appointment Query Service
 * <p>
 * Defines methods for querying workshop appointments based on various criteria.
 * </p>
 */
public interface AppointmentQueryService {
    /**
     * Handles retrieving an appointment by its ID.
     * 
     * @param query The {@link GetAppointmentByIdQuery} query containing the
     *              appointment ID
     * @return an optional WorkshopAppointment
     */
    Optional<Appointment> handle(GetAppointmentByIdQuery query);

    /**
     * Handles retrieving appointments by workshop ID and date range.
     * 
     * @param query The {@link GetAppointmentsByWorkshopAndRangeQuery} query
     *              containing workshop ID and date range
     * @return a list of WorkshopAppointments
     */
    List<Appointment> handle(GetAppointmentsByWorkshopAndRangeQuery query);

    /**
     * Handles retrieving appointments by driver ID.
     * 
     * @param query The {@link GetAppointmentsByDriverIdQuery} query containing
     *              driver ID
     * @return a list of Appointments
     */
    List<Appointment> handle(GetAppointmentsByDriverIdQuery query);
}
