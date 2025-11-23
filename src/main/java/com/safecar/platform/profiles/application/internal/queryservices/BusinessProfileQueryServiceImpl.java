package com.safecar.platform.profiles.application.internal.queryservices;

import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.services.BusinessProfileQueryService;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessProfileRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of BusinessProfileQueryService
 */
@Service
public class BusinessProfileQueryServiceImpl implements BusinessProfileQueryService {

    /**
     * The BusinessProfile repository
     */
    private final BusinessProfileRepository businessProfileRepository;

    public BusinessProfileQueryServiceImpl(BusinessProfileRepository businessProfileRepository) {
        this.businessProfileRepository = businessProfileRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BusinessProfile> handle(GetBusinessProfileByIdQuery query) {
        return businessProfileRepository.findById(query.id());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BusinessProfile> handle(GetBusinessProfileByUserEmailQuery query) {
        return businessProfileRepository.findByUserEmail(query.userEmail());
    }
}