package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.commands.SeedSpecializationsCommand;

/**
 * Specialization Command Service
 * <p>
 * Handles commands related to specializations within the workshop domain.
 * </p>
 * 
 */
public interface SpecializationCommandService {

    /**
     * Handles the seed specializations command.
     * <p>
     * This method processes the command to seed default specializations into the system.
     * </p>
     *
     * @param command the {@link SeedSpecializationsCommand} to handle
     */
    void handle(SeedSpecializationsCommand command);
}