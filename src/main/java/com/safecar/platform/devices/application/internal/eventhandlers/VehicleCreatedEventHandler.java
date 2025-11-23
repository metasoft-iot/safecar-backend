package com.safecar.platform.devices.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safecar.platform.devices.domain.model.events.VehicleCreatedEvent;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.devices.domain.model.commands.UpdateNumberOfDriverVehiclesCommand;

@Component
public class VehicleCreatedEventHandler {

    public final DriverCommandService commandService;

    public VehicleCreatedEventHandler(DriverCommandService commandService) {
        this.commandService = commandService;
    }

    @EventListener
    public void on(VehicleCreatedEvent event) {
        var command = new UpdateNumberOfDriverVehiclesCommand(event.driverId());
        commandService.handle(command);
    }
}
