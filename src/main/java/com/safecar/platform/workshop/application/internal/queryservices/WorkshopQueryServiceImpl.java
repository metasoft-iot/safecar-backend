package com.safecar.platform.workshop.application.internal.queryservices;

import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.services.WorkshopQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Workshop Query Service Implementation
 */
@Service
public class WorkshopQueryServiceImpl implements WorkshopQueryService {

    private final WorkshopRepository workshopRepository;

    public WorkshopQueryServiceImpl(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    /**
     * Handle the retrieval of a Workshop by its ID.
     * 
     * @param query the {@link GetWorkshopByIdQuery} instance
     * @return an {@link Optional} of {@link Workshop} if found
     */
    @Override
    public Optional<Workshop> handle(GetWorkshopByIdQuery query) {
        return workshopRepository.findById(query.workshopId());
    }

    @Override
    public Optional<Workshop> handle(
            com.safecar.platform.workshop.domain.model.queries.GetWorkshopByBusinessProfileIdQuery query) {
        return workshopRepository.findByBusinessProfileId(
                new com.safecar.platform.shared.domain.model.valueobjects.ProfileId(query.businessProfileId()));
    }
}