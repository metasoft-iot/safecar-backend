package com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import java.time.Instant;
import java.util.List;

/**
 * Repository for querying workshop appointments.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Finds all Appointments for a given WorkshopId.
     * @param workshopId the ID of the workshop
     * @return list of Appointments associated with the specified workshop
     */
    List<Appointment> findByWorkshopId(WorkshopId workshopId);

    /**
     * Finds all Appointments for a given VehicleId.
     * @param vehicleId the ID of the vehicle
     * @return list of Appointments associated with the specified vehicle
     */
    List<Appointment> findByVehicleId(VehicleId vehicleId);

    /**
     * Finds all Appointments for a given DriverId.
     * @param driverId the ID of the driver
     * @return list of Appointments associated with the specified driver
     */
    List<Appointment> findByDriverId(DriverId driverId);

    /**
     * Finds all Appointments with a specific status.
     * @param status the status of the appointment
     * @return list of Appointments with the specified status
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Finds all Appointments scheduled between the specified start and end dates.
     * @param startDate the start date of the appointment
     * @param endDate the end date of the appointment
     * @return list of Appointments scheduled between the specified dates
     */
    List<Appointment> findByScheduledAtStartAtBetween(Instant startDate, Instant endDate);

    /**
     * Finds all Appointments for a specific WorkshopId scheduled between the specified start and end dates.
     * @param workshopId the ID of the workshop
     * @param startDate the start date of the appointment
     * @param endDate the end date of the appointment
     * @return list of Appointments for the specified workshop scheduled between the specified dates
     */
    List<Appointment> findByWorkshopIdAndScheduledAtStartAtBetween(WorkshopId workshopId, Instant startDate, Instant endDate);
}