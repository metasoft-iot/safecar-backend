package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.commands.SeedSpecializationsCommand;
import com.safecar.platform.workshop.domain.services.SpecializationCommandService;

import java.sql.Timestamp;
import org.slf4j.Logger;

/**
 * Event handler for the ApplicationReadyEvent.
 * <p>
 *  This event is triggered when the application is ready to serve requests.
 *  It is used to seed the specializations in the database.
 * </p>
 * 
 * @author SafeCar Platform Team
 * @version 1.0
 * @since 2025-11-07
 */
@Service("workshopApplicationReadyEventHandler")
public class ApplicationReadyEventHandler {
    private final SpecializationCommandService specializationCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

    /**
     * Constructor.
     *
     * @param specializationCommandService the {@link SpecializationCommandService} specialization command service.
     */
    public ApplicationReadyEventHandler(SpecializationCommandService specializationCommandService) {
        this.specializationCommandService = specializationCommandService;
    }

    /**
     * Event listener for the ApplicationReadyEvent.
     * <p>
     *     This method is triggered when the application is ready to serve requests.
     *     It is used to seed the specializations in the database.
     * </p>
     *
     * @param event the {@link ApplicationReadyEvent} event.
     */
    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting to verify if specializations seeding is needed for {} at {}", applicationName, currentTimestamp());
        var seedSpecializationsCommand = new SeedSpecializationsCommand();
        specializationCommandService.handle(seedSpecializationsCommand);
        LOGGER.info("Specializations seeding verification finished for {} at {}", applicationName, currentTimestamp());
    }

    /**
     * Get the current timestamp.
     *
     * @return the current timestamp.
     */
    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}