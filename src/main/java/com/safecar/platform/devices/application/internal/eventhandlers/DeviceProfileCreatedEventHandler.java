package com.safecar.platform.devices.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

/**
 * Profile Created Event Handler for Devices BC
 * <p>
 * This event handler listens for {@link ProfileCreatedEvent} events and
 * automatically creates a basic Driver entity when the user has ROLE_DRIVER.
 * </p>
 */
@Component
public class DeviceProfileCreatedEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(DeviceProfileCreatedEventHandler.class);
    
    private final DriverCommandService commandService;

    /**
     * Constructor
     * 
     * @param commandService the {@link DriverCommandService} instance
     */
    public DeviceProfileCreatedEventHandler(DriverCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Handles the {@link ProfileCreatedEvent} by creating a basic Driver if the user has ROLE_DRIVER.
     * The driver can be updated later through Devices BC endpoints.
     * 
     * @param event the {@link ProfileCreatedEvent} instance
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ProfileCreatedEvent event) {
        logger.info("ProfileCreatedEvent received for profileId: {} with roles: {}", 
                    event.profileId(), event.userRoles());
        
        var isDriver = event.userRoles().contains("ROLE_DRIVER");

        if (isDriver) {
            try {
                logger.info("Creating driver for profileId: {}", event.profileId());
                
                var command = new CreateDriverCommand(event.profileId());
                var result = commandService.handle(command);
                
                if (result.isPresent()) {
                    logger.info("Driver created successfully with ID: {} for profileId: {}", 
                               result.get().getId(), event.profileId());
                } else {
                    logger.error("Failed to create driver for profileId: {} - command returned empty", 
                                event.profileId());
                }
            } catch (Exception e) {
                logger.error("Exception while creating driver for profileId: {}", 
                            event.profileId(), e);
            }
        } else {
            logger.debug("User does not have ROLE_DRIVER, skipping driver creation for profileId: {}", 
                        event.profileId());
        }
    }
}