package com.safecar.platform.workshop.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.commands.SeedSpecializationsCommand;
import com.safecar.platform.workshop.domain.model.entities.Specialization;
import com.safecar.platform.workshop.domain.model.valueobjects.Specializations;
import com.safecar.platform.workshop.domain.services.SpecializationCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.SpecializationRepository;

import java.util.Arrays;

/**
 * SpecializationCommandServiceImpl
 * <p>
 * Implementation of SpecializationCommandService.
 * This class is responsible for handling the SeedSpecializationsCommand and persisting
 * the specializations in the database.
 * </p>
 * 
 * @author SafeCar Platform Team
 * @version 1.0
 * @since 2025-11-07
 */
@Service
public class SpecializationCommandServiceImpl implements SpecializationCommandService {
    private final SpecializationRepository specializationRepository;

    /**
     * Constructor
     * 
     * @param specializationRepository {@link SpecializationRepository} instance
     */
    public SpecializationCommandServiceImpl(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    // inherited javadoc
    @Override
    public void handle(SeedSpecializationsCommand command) {
        Arrays.stream(Specializations.values()).forEach(specialization -> {
            if (!specializationRepository.existsByName(specialization))
                specializationRepository.save(new Specialization(Specializations.valueOf(specialization.name())));
        });
    }
}