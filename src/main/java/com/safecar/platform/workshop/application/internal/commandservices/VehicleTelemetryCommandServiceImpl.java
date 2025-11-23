package com.safecar.platform.workshop.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safecar.platform.workshop.application.internal.outboundservices.acl.ExternalDeviceService;
import com.safecar.platform.workshop.domain.model.aggregates.VehicleTelemetry;
import com.safecar.platform.workshop.domain.model.commands.FlushTelemetryCommand;
import com.safecar.platform.workshop.domain.model.commands.IngestTelemetrySampleCommand;
import com.safecar.platform.workshop.domain.services.VehicleTelemetryCommandService;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.VehicleTelemetryRepository;

/**
 * Enhanced command service implementation that persists TelemetryRecord
 * entities
 * with vehicle validation through the ExternalDeviceService ACL.
 *
 * Note: This implementation persists TelemetryRecord directly via the
 * TelemetryRecordRepository (instead of instantiating the VehicleTelemetry
 * aggregate) to keep behavior simple and ensure records are stored.
 */
@Service
public class VehicleTelemetryCommandServiceImpl implements VehicleTelemetryCommandService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleTelemetryCommandServiceImpl.class);

    private final VehicleTelemetryRepository vehicleTelemetryRepository;
    private final ExternalDeviceService externalDeviceService;

    public VehicleTelemetryCommandServiceImpl(VehicleTelemetryRepository vehicleTelemetryRepository,
            ExternalDeviceService externalDeviceService) {
        this.vehicleTelemetryRepository = vehicleTelemetryRepository;
        this.externalDeviceService = externalDeviceService;
    }

    @Override
    @Transactional
    public void handle(IngestTelemetrySampleCommand command) {
        logger.info("Processing telemetry ingestion command for vehicle: {}", command.sample().vehicleId());

        var sample = command.sample();

        try {
            // Validate that the vehicle exists in the Devices context before ingesting
            // telemetry
            externalDeviceService.validateVehicleExists(sample.vehicleId());
            logger.debug("Vehicle validation successful for vehicleId: {}", sample.vehicleId());

            var telemetry = vehicleTelemetryRepository.findByVehicleId(sample.vehicleId())
                    .orElseGet(() -> new VehicleTelemetry(sample.vehicleId()));

            telemetry.ingest(sample);
            vehicleTelemetryRepository.save(telemetry);

            logger.info("Successfully ingested telemetry record for vehicle: {}", sample.vehicleId());

        } catch (Exception e) {
            logger.error("Failed to ingest telemetry for vehicle: {} - Error: {}",
                    sample.vehicleId(), e.getMessage());
            throw e; // Re-throw to maintain transactional behavior
        }
    }

    @Override
    @Transactional
    public long handle(FlushTelemetryCommand command) {
        var telemetryAggregateId = command.telemetryAggregateId();
        var telemetry = vehicleTelemetryRepository.findById(telemetryAggregateId);

        if (telemetry.isPresent()) {
            var count = telemetry.get().flush();
            vehicleTelemetryRepository.save(telemetry.get());
            return count;
        }
        return 0;
    }
}
