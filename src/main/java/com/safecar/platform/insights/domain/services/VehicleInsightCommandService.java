package com.safecar.platform.insights.domain.services;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;

public interface VehicleInsightCommandService {
    VehicleInsight handle(GenerateVehicleInsightCommand command);
}
