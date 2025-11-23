package com.safecar.platform.insights.application.internal.queryservices;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.queries.GetVehicleInsightByVehicleIdQuery;
import com.safecar.platform.insights.domain.services.VehicleInsightQueryService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.VehicleInsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleInsightQueryServiceImpl implements VehicleInsightQueryService {

    private final VehicleInsightRepository repository;

    @Override
    public Optional<VehicleInsight> handle(GetVehicleInsightByVehicleIdQuery query) {
        return repository.findTopByVehicleVehicleIdOrderByGeneratedAtDesc(query.vehicleId());
    }
}
