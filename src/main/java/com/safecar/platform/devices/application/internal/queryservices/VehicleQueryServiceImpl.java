package com.safecar.platform.devices.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehiclesByWorkshopIdQuery;

import com.safecar.platform.devices.domain.services.VehicleQueryService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import com.safecar.platform.workshop.domain.model.valueobjects.WorkshopId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Vehicle Query Service Implementation
 * <p>
 * This class implements the VehicleQueryService interface to handle queries
 * related to Vehicle entities.
 * </p>
 */
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {
    /**
     * The Vehicle Repository
     */
    private final VehicleRepository vehicleRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * Constructor for VehicleQueryServiceImpl
     *
     * @param vehicleRepository     The Vehicle Repository
     * @param appointmentRepository The Appointment Repository
     */
    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository, AppointmentRepository appointmentRepository) {
        this.vehicleRepository = vehicleRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // {@inheritDoc}
    @Override
    public List<Vehicle> handle(GetVehicleByDriverIdQuery query) {
        return vehicleRepository.findByDriverId_DriverId(query.driverId());
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }

    // {@inheritDoc}
    @Override
    public List<Vehicle> handle(GetVehiclesByWorkshopIdQuery query) {
        // Get all appointments for the workshop
        var workshopId = new WorkshopId(query.workshopId());
        var appointments = appointmentRepository.findByWorkshopId(workshopId);

        // Extract unique vehicle IDs
        var vehicleIds = appointments.stream()
                .map(appointment -> appointment.getVehicleId().vehicleId())
                .distinct()
                .collect(Collectors.toList());

        // Fetch vehicles by IDs
        return vehicleRepository.findAllById(vehicleIds);
    }

    @Override
    public Optional<Vehicle> handle(
            com.safecar.platform.devices.domain.model.queries.GetVehicleByLicensePlateQuery query) {
        return vehicleRepository.findByLicensePlate(query.licensePlate());
    }
}
