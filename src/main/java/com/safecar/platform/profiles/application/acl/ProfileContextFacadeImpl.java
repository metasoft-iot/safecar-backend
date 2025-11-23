package com.safecar.platform.profiles.application.acl;

import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.services.BusinessProfileQueryService;
import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import org.springframework.stereotype.Service;

/**
 * Implementation of the Profiles Context Facade (Anti-Corruption Layer).
 * 
 * This service provides a stable interface for external bounded contexts
 * to interact with the Profiles domain, ensuring loose coupling and
 * protecting internal domain model changes.
 */
@Service
public class ProfileContextFacadeImpl implements ProfilesContextFacade {
    private final PersonProfileQueryService personProfileQueryService;
    private final BusinessProfileQueryService businessProfileQueryService;

    /**
     * Constructor for ProfileContextFacadeImpl.
     *
     * @param personProfileQueryService   person profile query service
     * @param businessProfileQueryService business profile query service
     */
    public ProfileContextFacadeImpl(PersonProfileQueryService personProfileQueryService,
                                   BusinessProfileQueryService businessProfileQueryService) {
        this.personProfileQueryService = personProfileQueryService;
        this.businessProfileQueryService = businessProfileQueryService;
    }

    // inherited javadoc
    @Override
    public boolean existsPersonProfileByUserEmail(String userEmail) {
        var getUserProfileQuery = new GetPersonProfileByUserEmailQuery(userEmail);
        var existing = personProfileQueryService.handle(getUserProfileQuery);
        return existing.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean existsPersonProfileById(Long profileId) {
        var getProfileByIdQuery = new GetPersonProfileByIdQuery(profileId);
        var existing = personProfileQueryService.handle(getProfileByIdQuery);
        return existing.isPresent();
    }

    // inherited javadoc
    @Override
    public Long getPersonProfileIdByUserEmail(String userEmail) {
        var getUserProfileQuery = new GetPersonProfileByUserEmailQuery(userEmail);
        var person = personProfileQueryService.handle(getUserProfileQuery);
        return person.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    // inherited javadoc
    @Override
    public boolean existsBusinessProfileByUserEmail(String userEmail) {
        var getUserProfileQuery = new GetBusinessProfileByUserEmailQuery(userEmail);
        var existing = businessProfileQueryService.handle(getUserProfileQuery);
        return existing.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean existsBusinessProfileById(Long profileId) {
        var getProfileByIdQuery = new GetBusinessProfileByIdQuery(profileId);
        var existing = businessProfileQueryService.handle(getProfileByIdQuery);
        return existing.isPresent();
    }

    // inherited javadoc
    @Override
    public Long getBusinessProfileIdByUserEmail(String userEmail) {
        var getUserProfileQuery = new GetBusinessProfileByUserEmailQuery(userEmail);
        var business = businessProfileQueryService.handle(getUserProfileQuery);
        return business.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    // inherited javadoc
    @Override
    public String getBusinessNameByProfileId(Long profileId) {
        var getProfileByIdQuery = new GetBusinessProfileByIdQuery(profileId);
        var business = businessProfileQueryService.handle(getProfileByIdQuery);
        return business.map(bp -> bp.getBusinessName()).orElse(null);
    }

    // inherited javadoc
    @Override
    public String getBusinessAddressByProfileId(Long profileId) {
        var getProfileByIdQuery = new GetBusinessProfileByIdQuery(profileId);
        var business = businessProfileQueryService.handle(getProfileByIdQuery);
        return business.map(bp -> bp.getBusinessAddress()).orElse(null);
    }

    // inherited javadoc
    @Override
    public String getBusinessContactPhoneByProfileId(Long profileId) {
        var getProfileByIdQuery = new GetBusinessProfileByIdQuery(profileId);
        var business = businessProfileQueryService.handle(getProfileByIdQuery);
        return business.map(bp -> bp.getContactPhone()).orElse(null);
    }

    // inherited javadoc
    @Override
    public String getPersonFullNameByProfileId(Long profileId) {
        var getProfileByIdQuery = new GetPersonProfileByIdQuery(profileId);
        var person = personProfileQueryService.handle(getProfileByIdQuery);
        return person.map(pp -> pp.getFullName()).orElse("Unknown Driver");
    }
}