package com.safecar.platform.devices.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.safecar.platform.devices.application.internal.outboundservices.acl.ExternalProfileService;
import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.DeleteVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateVehicleCommand;
import com.safecar.platform.devices.domain.model.events.VehicleCreatedEvent;
import com.safecar.platform.devices.domain.services.VehicleCommandService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.Optional;

/**
 * Vehicle Command Service Implementation with ACL Integration
 * <p>
 * This service handles commands related to vehicle management, including
 * creation, updating, and deletion of vehicles. It integrates with the
 * Profiles bounded context through ACL for driver validation.
 * </p>
 */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final ExternalProfileService externalProfileService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Constructor for VehicleCommandServiceImpl
     * 
     * @param vehicleRepository         Vehicle Repository
     * @param externalProfileService    External Profile Service (ACL)
     * @param applicationEventPublisher Application Event Publisher
     */
    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository,
            ExternalProfileService externalProfileService,
            ApplicationEventPublisher eventPublisher) {
        this.vehicleRepository = vehicleRepository;
        this.externalProfileService = externalProfileService;
        this.eventPublisher = eventPublisher;
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        if (!externalProfileService.validatePersonProfileExists(command.driverId()))
            throw new IllegalArgumentException("Driver profile not found for ID: " + command.driverId());

        if (vehicleRepository.existsByLicensePlate(command.licensePlate()))
            throw new IllegalArgumentException(
                    "Vehicle with license plate '" + command.licensePlate() + "' already exists");

        var vehicle = new Vehicle(command);
        var vehicleCreated = vehicleRepository.save(vehicle);

        eventPublisher.publishEvent(new VehicleCreatedEvent(
                vehicleCreated.getId(),
                command.driverId()));

        return Optional.of(vehicleCreated);
    }

    // {@inheritDoc}
    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        try {
            var vehicleId = command.vehicleId();
            var vehicle = this.vehicleRepository.findById(vehicleId)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId()));

            // Check for license plate conflicts (if changed)
            if (!vehicle.getLicensePlate().equals(command.licensePlate()) &&
                    vehicleRepository.existsByLicensePlate(command.licensePlate())) {
                throw new IllegalArgumentException(
                        "Vehicle with license plate '" + command.licensePlate() + "' already exists");
            }

            vehicle.updateVehicle(command.licensePlate(), command.brand(), command.model());
            var vehicleUpdated = vehicleRepository.save(vehicle);

            return Optional.of(vehicleUpdated);

        } catch (Exception e) {
            throw e; // Re-throw to maintain transactional behavior
        }
    }

    // {@inheritDoc}
    @Override
    public void handle(DeleteVehicleCommand command) {
        try {
            var exists = this.vehicleRepository.existsById(command.vehicleId());
            if (!exists)
                throw new IllegalArgumentException("Vehicle not found with ID: " + command.vehicleId());

            // Note: Future enhancement could include validation to check if vehicle has
            // active
            // workshop operations before allowing deletion through workshop ACL integration

            this.vehicleRepository.deleteById(command.vehicleId());

        } catch (Exception e) {
            throw e;
        }
    }
}