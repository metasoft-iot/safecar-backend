package com.safecar.platform.workshop.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalIamService;
import com.safecar.platform.workshop.domain.model.aggregates.Appointment;
import com.safecar.platform.workshop.domain.model.commands.*;
import com.safecar.platform.workshop.domain.model.valueobjects.AppointmentStatus;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceTypes;
import com.safecar.platform.workshop.domain.services.AppointmentCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.ServiceTypeRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.MechanicRepository;

import java.util.Optional;

/**
 * Appointment Command Service Implementation.
 * Handles commands for appointment management including mechanic assignment.
 */
@Service
@Transactional
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository repository;
    private final MechanicRepository mechanicRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ExternalIamService externalIamService;
    private final ExternalDeviceService externalDeviceService;

    public AppointmentCommandServiceImpl(AppointmentRepository repository,
            MechanicRepository mechanicRepository,
            ServiceTypeRepository serviceTypeRepository,
            ExternalIamService externalIamService,
            ExternalDeviceService externalDeviceService) {
        this.repository = repository;
        this.mechanicRepository = mechanicRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.externalIamService = externalIamService;
        this.externalDeviceService = externalDeviceService;
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(CreateAppointmentCommand command) {
        // Validate driver exists in IAM context
        var driverId = command.driverId().driverId();
        if (!externalIamService.validateDriverExists(driverId))
            throw new IllegalArgumentException("Driver with ID " + driverId + " does not exist");

        // Validate vehicle exists in Devices context
        var vehicleId = command.vehicleId();
        if (!externalDeviceService.validateVehicleExists(vehicleId))
            throw new IllegalArgumentException("Vehicle with ID " + vehicleId + " does not exist");

        // Validate driver owns vehicle
        if (!externalDeviceService.validateDriverOwnsVehicle(vehicleId, driverId))
            throw new IllegalArgumentException("Driver " + driverId + " does not own vehicle " + vehicleId);

        // Fetch ServiceType from database (MUST be persisted entity, not transient)
        var requestedServiceType = command.serviceType();
        var persistedServiceType = requestedServiceType;
        
        if (requestedServiceType != null && requestedServiceType.getName() != null) {
            // Replace transient ServiceType with persisted one from DB
            persistedServiceType = serviceTypeRepository.findByName(requestedServiceType.getName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Service type " + requestedServiceType.getName() + " not found in database"));
        } else {
            // Use default service type if none provided
            persistedServiceType = serviceTypeRepository.findByName(ServiceTypes.GENERAL_MAINTENANCE)
                    .orElseThrow(() -> new IllegalStateException(
                            "Default service type GENERAL_MAINTENANCE not found in database"));
        }

        // Check for slot overlaps with existing appointments in the same workshop
        var existingAppointments = repository.findByWorkshopId(command.workshopId());
        var hasOverlap = existingAppointments.stream()
                .filter(a -> !AppointmentStatus.CANCELLED.equals(a.getStatus()))
                .anyMatch(a -> a.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("Appointment slot overlaps with existing appointment");

        // Create command with persisted ServiceType
        var commandWithPersistedServiceType = new CreateAppointmentCommand(
                command.workshopId(),
                command.vehicleId(),
                command.driverId(),
                command.slot(),
                persistedServiceType,  // ← Persisted from DB
                command.customServiceDescription()
        );

        // Create and save appointment
        var appointment = new Appointment(commandWithPersistedServiceType);
        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(RescheduleAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var existingAppointments = repository
                .findByWorkshopId(appointment.getWorkshopId());

        var hasOverlap = existingAppointments.stream()
                .filter(a -> !a.getId().equals(appointment.getId()))
                .filter(a -> !AppointmentStatus.CANCELLED.equals(a.getStatus()))
                .anyMatch(a -> a.getScheduledAt().overlapsWith(command.slot()));

        if (hasOverlap)
            throw new IllegalStateException("New appointment slot overlaps with existing appointment");

        appointment.reschedule(command.slot());

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(UpdateAppointmentStatusCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        var updated = appointment.updateAppointmentStatus(command.status());

        if (!updated)
            throw new IllegalStateException(
                    "Appointment status is already " + command.status() + " or transition is invalid.");

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public void handle(AddAppointmentNoteCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.addNote(command.content(), command.authorId());

        repository.save(appointment);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(AssignMechanicToAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        // Validate mechanic exists in the system
        var mechanic = mechanicRepository.findById(command.mechanicId())
                .orElseThrow(() -> new IllegalArgumentException("Mechanic with ID " + command.mechanicId() + " does not exist"));

        // Validate mechanic belongs to the same workshop as the appointment
        if (mechanic.getWorkshopIdValue() == null) {
            throw new IllegalStateException("Mechanic is not assigned to any workshop yet");
        }

        if (!mechanic.getWorkshopIdValue().equals(appointment.getWorkshopId().workshopId())) {
            throw new IllegalStateException(
                "Cannot assign mechanic from workshop " + mechanic.getWorkshopIdValue() + 
                " to appointment in workshop " + appointment.getWorkshopId().workshopId());
        }

        appointment.assignMechanic(command.mechanicId());

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

    // {@inheritDoc}
    @Override
    public Optional<Appointment> handle(UnassignMechanicFromAppointmentCommand command) {
        var appointment = repository.findById(command.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.unassignMechanic();

        var saved = repository.save(appointment);

        return Optional.of(saved);
    }

}