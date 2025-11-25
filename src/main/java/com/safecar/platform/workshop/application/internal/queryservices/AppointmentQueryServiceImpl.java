package com.safecar.platform.workshop.application.internal.queryservices;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.queries.*;
import com.safecar.platform.workshop.domain.services.AppointmentQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.AppointmentRepository;

/**
 * Workshop Appointment Query Service Implementation
 * 
 * @see AppointmentQueryService WorkshopAppointmentQueryService for method
 *      details.
 */
@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

    /**
     * Repository for WorkshopAppointment to handle persistence operations.
     */
    private final AppointmentRepository repository;

    public AppointmentQueryServiceImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Appointment> handle(GetAppointmentByIdQuery query) {
        return repository.findById(query.appointmentId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> handle(GetAppointmentsByWorkshopAndRangeQuery query) {
        // If both from and to are null, return all appointments for the workshop
        if (query.from() == null && query.to() == null) {
            return repository.findByWorkshopId(query.workshopId());
        }

        // If only one of them is null, we need special handling
        // If from is null, use a very early date; if to is null, use a far future date
        Instant fromDate = query.from() != null ? query.from() : Instant.EPOCH;
        Instant toDate = query.to() != null ? query.to() : Instant.ofEpochSecond(4102444800L); // 2100-01-01

        return repository.findByWorkshopIdAndScheduledAtStartAtBetween(
                query.workshopId(),
                fromDate,
                toDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> handle(GetAppointmentsByDriverIdQuery query) {
        return repository.findByDriverId(query.driverId());
    }
}