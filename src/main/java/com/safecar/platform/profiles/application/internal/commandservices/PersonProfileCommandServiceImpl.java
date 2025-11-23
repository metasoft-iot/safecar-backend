package com.safecar.platform.profiles.application.internal.commandservices;

import com.safecar.platform.profiles.application.internal.outbounceservices.acl.ExternalIamService;
import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;
import com.safecar.platform.shared.domain.model.events.ProfileCreatedEvent;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PersonProfileCommandService
 */
@Service
public class PersonProfileCommandServiceImpl implements PersonProfileCommandService {

    private static final Logger logger = LoggerFactory.getLogger(PersonProfileCommandServiceImpl.class);
    
    /**
     * The PersonProfile repository
     */
    private final PersonProfileRepository personProfileRepository;
    /**
     * The Application Event Publisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * The External IAM Service
     */
    private final ExternalIamService externalIamService;

    public PersonProfileCommandServiceImpl(
            PersonProfileRepository personProfileRepository,
            ApplicationEventPublisher applicationEventPublisher,
            ExternalIamService externalIamService) {
        this.personProfileRepository = personProfileRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.externalIamService = externalIamService;
    }

    // javadoc inherited
    @Transactional
    @Override
    public Optional<PersonProfile> handle(CreatePersonProfileCommand command, String userEmail) {

        logger.info("Creating PersonProfile for user: {}", userEmail);
        
        var userRoles = externalIamService.fetchUserRolesByUserEmail(userEmail);
        logger.info("Fetched roles for user {}: {}", userEmail, userRoles);

        var profile = new PersonProfile(
                userEmail,
                command.fullName(),
                command.city(),
                command.country(),
                new Phone(command.phone()),
                new Dni(command.dni()));

        var saved = personProfileRepository.save(profile);
        logger.info("PersonProfile saved with ID: {} for user: {}", saved.getId(), userEmail);

        var event = new ProfileCreatedEvent(
                saved.getId(),
                userRoles);

        logger.info("Publishing ProfileCreatedEvent for profileId: {} with roles: {}", 
                   saved.getId(), userRoles);
        applicationEventPublisher.publishEvent(event);

        return Optional.of(saved);
    }

    // javadoc inherited
    @Transactional
    @Override
    public Optional<PersonProfile> handle(UpdatePersonProfileCommand command, Long personProfileId) {

        var personProfile = personProfileRepository.findById(personProfileId);

        if (!personProfile.isPresent())
            throw new IllegalArgumentException("PersonProfile with id " + command.personId() + " not found");

        var entity = personProfile.get();

        entity.updatePersonProfileMetrics(
                command.fullName(),
                command.city(),
                command.country(),
                command.phone(),
                command.dni());

        var updated = personProfileRepository.save(entity);

        return Optional.of(updated);
    }
}
