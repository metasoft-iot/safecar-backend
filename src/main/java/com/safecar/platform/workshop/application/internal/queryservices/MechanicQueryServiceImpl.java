package com.safecar.platform.workshop.application.internal.queryservices;

import com.safecar.platform.workshop.domain.model.aggregates.Mechanic;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicByProfileIdQuery;
import com.safecar.platform.workshop.domain.services.MechanicQueryService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MechanicQueryServiceImpl implements MechanicQueryService {
    private final MechanicRepository mechanicRepository;

    public MechanicQueryServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public Optional<Mechanic> handle(GetMechanicByProfileIdQuery query) {
        return mechanicRepository.findByProfileId_ProfileId(query.profileId());
    }

    @Override
    public java.util.List<Mechanic> handle(
            com.safecar.platform.workshop.domain.model.queries.GetMechanicsByWorkshopIdQuery query) {
        return mechanicRepository.findByWorkshopId(query.workshopId());
    }
}