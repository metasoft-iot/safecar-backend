package com.safecar.platform.workshop.domain.services;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.model.commands.AssignMechanicToWorkshopCommand;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateMechanicMetricsCommand;

import java.util.Optional;

/**
 * Mechanic Command Service
 * <p>
 * Handles commands related to mechanic operations such as creation and
 * updating metrics.
 * </p>
 */
public interface MechanicCommandService {
    /**
     * Handles the creation of a new mechanic based on the provided command.
     * 
     * @param command The {@link CreateMechanicCommand} command containing mechanic
     *                creation details
     * @return an Optional containing the created Mechanic, or empty if creation
     *         failed
     */
    Optional<Mechanic> handle(CreateMechanicCommand command);

    /**
     * Handles the update of an existing mechanic based on the provided command.
     * 
     * @param command   The {@link UpdateMechanicMetricsCommand} command containing
     *                  mechanic update details
     * @param mechanicId the ID of the mechanic to update
     * @return an Optional containing the updated Mechanic, or empty if update
     *         failed
     */
    Optional<Mechanic> handle(UpdateMechanicMetricsCommand command, Long mechanicId);

    /**
     * Handles assigning a mechanic to a workshop.
     * 
     * @param command The {@link AssignMechanicToWorkshopCommand} command
     * @return an Optional containing the updated Mechanic, or empty if assignment failed
     */
    Optional<Mechanic> handle(AssignMechanicToWorkshopCommand command);
}