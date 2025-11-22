package com.safecar.platform.insights.application.internal.commandservices;

import com.safecar.platform.insights.application.internal.outboundservices.analytics.TelemetryAnalyticsGateway;
import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.services.VehicleInsightCommandService;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.VehicleInsightRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleInsightCommandServiceImpl implements VehicleInsightCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleInsightCommandServiceImpl.class);

    private final VehicleInsightRepository repository;
    private final TelemetryAnalyticsGateway analyticsGateway;
    private final com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade workshopContextFacade;

    @Override
    @Transactional
    public VehicleInsight handle(GenerateVehicleInsightCommand command) {
        var sample = workshopContextFacade.fetchTelemetrySample(command.telemetryId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Telemetry not found for ID: " + command.telemetryId()));

        var vehicle = new com.safecar.platform.insights.domain.model.valueobjects.VehicleReference(
                sample.driverId().driverId(),
                "Driver " + sample.driverId().driverId(), // Placeholder as we don't have name
                sample.vehicleId().vehicleId(),
                "VEH-" + sample.vehicleId().vehicleId() // Placeholder
        );

        var payload = new com.safecar.platform.insights.domain.model.valueobjects.TelemetrySensorPayload(
                sample.timestamp().occurredAt(),
                sample.cabinGasLevel() != null ? sample.cabinGasLevel().concentrationPpm() : null,
                null, // Engine Temp not in Workshop yet
                null, // Ambient Temp not in Workshop yet
                null, // Humidity not in Workshop yet
                null, // Current Amps not in Workshop yet
                sample.location() != null ? sample.location().latitude() : null,
                sample.location() != null ? sample.location().longitude() : null,
                sample.severity().name());

        var analysis = analyticsGateway.analyze(vehicle, payload);
        LOGGER.debug("Telemetry analytics completed for vehicle {} with risk {}",
                vehicle.vehicleId(), analysis.riskLevel());
        var insight = new VehicleInsight(vehicle, analysis);
        return repository.save(insight);
    }
}
