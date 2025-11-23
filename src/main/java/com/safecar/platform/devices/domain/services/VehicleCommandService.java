package com.safecar.platform.devices.domain.services;

import java.util.Optional;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.DeleteVehicleCommand;
import com.safecar.platform.devices.domain.model.commands.UpdateVehicleCommand;

/**
 * Vehicle Command Service Interface
 * <p>
 * This interface defines the contract for handling vehicle-related commands.
 * </p>
 */
public interface VehicleCommandService {
    /**
     * Handle Create Vehicle Command
     * 
     * @param command CreateVehicleCommand
     * @return Optional<Vehicle>
     */
    Optional<Vehicle> handle(CreateVehicleCommand command);

    /**
     * Handle Update Vehicle Command
     * 
     * @param command UpdateVehicleCommand
     * @return Optional<Vehicle>
     */
    Optional<Vehicle> handle(UpdateVehicleCommand command);

    /**
     * Handle Delete Vehicle Command
     * 
     * @param command DeleteVehicleCommand
     */
    void handle(DeleteVehicleCommand command);
}
