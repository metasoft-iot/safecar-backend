package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.application.internal.outbounceservices.acl.ExternalIamService;
import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdateBusinessProfileCommand;
import com.safecar.platform.profiles.domain.services.BusinessProfileCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessProfileRepository;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of BusinessProfileCommandService
 */
@Service
public class BusinessProfileCommandServiceImpl implements BusinessProfileCommandService {

    private final BusinessProfileRepository businessProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ExternalIamService externalIamService;

    public BusinessProfileCommandServiceImpl(
            BusinessProfileRepository businessProfileRepository,
            ApplicationEventPublisher applicationEventPublisher,
            ExternalIamService externalIamService) {
        this.businessProfileRepository = businessProfileRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.externalIamService = externalIamService;
    }

    // inheritDoc javadoc
    @Transactional
    @Override
    public Optional<BusinessProfile> handle(CreateBusinessProfileCommand command, String userEmail) {

        var userRoles = externalIamService.fetchUserRolesByUserEmail(userEmail);

        var businessProfile = new BusinessProfile(command, userEmail);

        var saved = businessProfileRepository.save(businessProfile);

        applicationEventPublisher.publishEvent(new ProfileCreatedEvent(
                saved.getId(),
                userRoles));

        return Optional.of(saved);
    }

    // inheritDoc javadoc
    @Transactional
    @Override
    public Optional<BusinessProfile> handle(UpdateBusinessProfileCommand command, Long businessProfileId) {

        var businessProfileOpt = businessProfileRepository.findById(businessProfileId);

        if (!businessProfileOpt.isPresent())
            throw new IllegalArgumentException("BusinessProfile with ID " + businessProfileId + " does not exist.");

        var entity = businessProfileOpt.get();

        entity.updateBusinessProfileMetrics(command.businessName(), command.ruc(), command.businessAddress(),
                command.contactPhone(), command.contactEmail(), command.description());

        var updated = businessProfileRepository.save(entity);

        return Optional.of(updated);
    }
}