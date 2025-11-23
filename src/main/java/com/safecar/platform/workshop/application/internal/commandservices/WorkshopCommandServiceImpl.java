package com.safecar.platform.workshop.application.internal.commandservices;

import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.commands.CreateWorkshopCommand;
import com.safecar.platform.workshop.domain.model.commands.UpdateWorkshopCommand;
import com.safecar.platform.workshop.domain.services.WorkshopCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Workshop Command Service Implementation
 */
@Service
public class WorkshopCommandServiceImpl implements WorkshopCommandService {

    private final WorkshopRepository workshopRepository;
    private final ProfilesContextFacade profilesContextFacade;

    public WorkshopCommandServiceImpl(WorkshopRepository workshopRepository, 
                                    ProfilesContextFacade profilesContextFacade) {
        this.workshopRepository = workshopRepository;
        this.profilesContextFacade = profilesContextFacade;
    }

    @Override
    public Optional<Workshop> handle(CreateWorkshopCommand command) {
        // Verify business profile exists
        var businessProfileId = new ProfileId(command.businessProfileId());
        if (!profilesContextFacade.existsBusinessProfileById(command.businessProfileId())) {
            throw new IllegalArgumentException("Business profile with ID " + command.businessProfileId() + " does not exist");
        }

        // Verify workshop doesn't already exist for this business profile
        if (workshopRepository.existsByBusinessProfileId(businessProfileId)) {
            throw new IllegalArgumentException("Workshop already exists for business profile ID " + command.businessProfileId());
        }

        // Create and save workshop
        var workshop = new Workshop(command);
        var savedWorkshop = workshopRepository.save(workshop);
        return Optional.of(savedWorkshop);
    }

    @Override
    public Optional<Workshop> handle(UpdateWorkshopCommand command) {
    Long id = command.workshopId();
    if (id == null) return Optional.empty();
    var workshopOpt = workshopRepository.findById(id);
        if (workshopOpt.isEmpty()) return Optional.empty();
        var workshop = workshopOpt.get();
        workshop.updateDescription(command.workshopDescription());
        var updated = workshopRepository.save(workshop);
        return Optional.of(updated);
    }
}