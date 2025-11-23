package com.safecar.platform.insights.domain.services;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.queries.GetVehicleInsightByVehicleIdQuery;

import java.util.Optional;

public interface VehicleInsightQueryService {
    Optional<VehicleInsight> handle(GetVehicleInsightByVehicleIdQuery query);
}
