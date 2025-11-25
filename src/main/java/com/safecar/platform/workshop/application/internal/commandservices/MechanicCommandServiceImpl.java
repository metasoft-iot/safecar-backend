package com.safecar.platform.workshop.application.internal.commandservices;

import org.springframework.stereotype.Service;
import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;
import com.safecar.platform.workshop.domain.model.commands.AssignMechanicToWorkshopCommand;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateMechanicMetricsCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.MechanicRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.SpecializationRepository;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

/**
 * Mechanic Command Service Implementation
 * <p>
 * This service handles commands related to mechanic management.
 * </p>
 */
@Service
@Transactional
public class MechanicCommandServiceImpl implements MechanicCommandService {

    private final MechanicRepository mechanicRepository;
    private final SpecializationRepository specializationRepository;
    private final WorkshopRepository workshopRepository;

    public MechanicCommandServiceImpl(
            MechanicRepository mechanicRepository,
            SpecializationRepository specializationRepository,
            WorkshopRepository workshopRepository) {
        this.mechanicRepository = mechanicRepository;
        this.specializationRepository = specializationRepository;
        this.workshopRepository = workshopRepository;
    }

    // inheritdoc javadoc
    @Override
    public Optional<Mechanic> handle(CreateMechanicCommand command) {
        var mechanic = Mechanic.create(command);

        // If no specializations provided, add default ENGINE specialization from DB
        if (mechanic.getSpecializations().isEmpty()) {
            var defaultSpec = specializationRepository.findByName(
                    com.safecar.platform.workshop.domain.model.valueobjects.Specializations.ENGINE)
                    .orElseGet(() -> {
                        // Create and save ENGINE if it doesn't exist (safety fallback)
                        var newSpec = new Specialization(
                                com.safecar.platform.workshop.domain.model.valueobjects.Specializations.ENGINE);
                        return specializationRepository.save(newSpec);
                    });
            mechanic.addSpecializations(Set.of(defaultSpec));
        }

        var saved = mechanicRepository.save(mechanic);
        return Optional.of(saved);
    }

    // inheritdoc javadoc
    @Override
    public Optional<Mechanic> handle(UpdateMechanicMetricsCommand command, Long mechanicId) {

        var specializations = command.specializations().stream()
                .map(specialization -> specializationRepository.findByName(specialization.getName())
                        .orElseThrow(
                                () -> new RuntimeException("Specialization not found: " + specialization.getName())))
                .collect(Collectors.toSet());

        if (specializations.isEmpty())
            throw new IllegalArgumentException("Mechanic must have at least one specialization.");

        var mechanicOpt = mechanicRepository.findById(mechanicId);

        if (mechanicOpt.isEmpty())
            return Optional.empty();

        var mechanic = mechanicOpt.get();
        mechanic.updateSpecializations(specializations);
        mechanic.updateYearsOfExperience(command.yearsOfExperience());

        var updated = mechanicRepository.save(mechanic);
        return Optional.of(updated);
    }

    // inheritdoc javadoc
    @Override
    public Optional<Mechanic> handle(AssignMechanicToWorkshopCommand command) {
        // Validate workshop exists
        var workshop = workshopRepository.findById(command.workshopId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Workshop with ID " + command.workshopId() + " does not exist"));

        // Validate mechanic exists
        var mechanic = mechanicRepository.findById(command.mechanicId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Mechanic with ID " + command.mechanicId() + " does not exist"));

        // Store previous workshop ID to decrement counter if reassigning
        var previousWorkshopId = mechanic.getWorkshopIdValue();

        // Assign mechanic to workshop
        mechanic.assignToWorkshop(command.workshopId());

        // Update workshop mechanic counters
        if (previousWorkshopId != null && !previousWorkshopId.equals(command.workshopId())) {
            // Mechanic is being moved from one workshop to another
            var previousWorkshop = workshopRepository.findById(previousWorkshopId);
            previousWorkshop.ifPresent(w -> {
                w.decrementMechanicsCount();
                workshopRepository.save(w);
            });
        }

        // Increment counter only if this is a new assignment (not already assigned to
        // this workshop)
        if (previousWorkshopId == null || !previousWorkshopId.equals(command.workshopId())) {
            workshop.incrementMechanicsCount();
            workshopRepository.save(workshop);
        }

        var updated = mechanicRepository.save(mechanic);
        return Optional.of(updated);
    }
}