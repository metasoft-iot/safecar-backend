package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateWorkshopCommand;

import java.util.Optional;

/**
 * Workshop Command Service
 */
public interface WorkshopCommandService {

    /**
     * Handle the creation of a new Workshop.
     * 
     * @param command the {@link CreateWorkshopCommand} instance
     * @return the created {@link Workshop} instance wrapped in Optional
     */
    Optional<Workshop> handle(CreateWorkshopCommand command);

    /**
     * Handle update of an existing Workshop (currently only description is mutable).
     * @param command the update command
     * @return updated workshop if found
     */
    Optional<Workshop> handle(UpdateWorkshopCommand command);
}