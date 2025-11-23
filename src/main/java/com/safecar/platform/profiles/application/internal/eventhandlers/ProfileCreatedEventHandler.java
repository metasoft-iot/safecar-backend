package com.safecar.platform.profiles.application.internal.eventhandlers;

import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

@Component("profilesProfileCreatedEventHandler")
public class ProfileCreatedEventHandler {

    private final Logger logger = Logger.getLogger(ProfileCreatedEvent.class.getName());

    @EventListener
    public void on(ProfileCreatedEvent event) {
        logger.info("Profile created: " + event.profileId());
    }   
}
