package com.safecar.platform.workshop.application.internal.eventhandlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;
import com.safecar.platform.workshop.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.workshop.domain.services.WorkshopCommandService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkshopProfileCreatedEventHandler {

    private final WorkshopCommandService commandService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ProfileCreatedEvent event) {
        var isWorkshop = event.userRoles()
                .contains("ROLE_WORKSHOP");

        if (isWorkshop) {
            var command = new CreateWorkshopCommand(
                    event.profileId(),
                    "Default workshop operated by business profile");

            commandService.handle(command);
        }
    }
}
