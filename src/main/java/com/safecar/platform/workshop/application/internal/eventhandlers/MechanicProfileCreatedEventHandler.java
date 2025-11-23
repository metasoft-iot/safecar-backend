package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;
import com.safecar.platform.workshop.domain.model.commands.CreateMechanicCommand;
import com.safecar.platform.workshop.domain.services.MechanicCommandService;

/**
 * Mechanic Profile Created Event Handler for Workshop BC
 * <p>
 * This event handler listens for {@link ProfileCreatedEvent} events and
 * automatically creates a basic Mechanic entity when the user has
 * ROLE_MECHANIC.
 * </p>
 */
@Component
public class MechanicProfileCreatedEventHandler {

    private final MechanicCommandService commandService;

    /**
     * Constructor
     * 
     * @param commandService the {@link MechanicCommandService} instance
     */
    public MechanicProfileCreatedEventHandler(MechanicCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Handles the {@link ProfileCreatedEvent} by creating a basic Mechanic
     * with default values when the user has ROLE_MECHANIC. The mechanic can be
     * updated later through Workshop BC endpoints to assign workshop and update metrics.
     * 
     * @param event the {@link ProfileCreatedEvent} instance
     */
    @EventListener
    public void on(ProfileCreatedEvent event) {
        var isMechanic = event.userRoles().contains("ROLE_MECHANIC");

        if (isMechanic) {

            var command = new CreateMechanicCommand(
                    event.profileId(),
                    null, 
                    null, 
                    0);

            commandService.handle(command);
        }
    }
}