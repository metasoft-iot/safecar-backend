package com.safecar.platform.devices.domain.services;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateNumberOfDriverVehiclesCommand;

import java.util.Optional;

/**
 * Driver Command Service Interface
 * <p>
 * This interface defines the contract for handling driver-related commands
 * within the Devices bounded context.
 * </p>
 */
public interface DriverCommandService {
    /**
     * Handles the creation of a new driver based on the provided command.
     * 
     * @param command the command containing driver creation details
     * @return an Optional containing the created Driver, or empty if creation
     *         failed
     */
    Optional<Driver> handle(CreateDriverCommand command);

    /**
     * Handles the update of the number of vehicles associated with a driver.
     * 
     * @param command the command containing driver ID and new vehicle count
     * @return an Optional containing the updated Driver, or empty if update failed
     */
    Optional<Driver> handle(UpdateNumberOfDriverVehiclesCommand command);
}