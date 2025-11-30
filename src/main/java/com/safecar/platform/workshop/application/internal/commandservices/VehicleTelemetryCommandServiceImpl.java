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
    private final com.safecar.platform.deviceManagement.domain.services.DeviceQueryService deviceQueryService;
    private final com.safecar.platform.devices.domain.services.VehicleQueryService vehicleQueryService;

    public VehicleTelemetryCommandServiceImpl(VehicleTelemetryRepository vehicleTelemetryRepository,
            ExternalDeviceService externalDeviceService,
            com.safecar.platform.deviceManagement.domain.services.DeviceQueryService deviceQueryService,
            com.safecar.platform.devices.domain.services.VehicleQueryService vehicleQueryService) {
        this.vehicleTelemetryRepository = vehicleTelemetryRepository;
        this.externalDeviceService = externalDeviceService;
        this.deviceQueryService = deviceQueryService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @Override
    @Transactional
    public void handle(IngestTelemetrySampleCommand command) {
        var sample = command.sample();

        // Si viene macAddress, resolvemos vehicleId y driverId
        if (command.macAddress() != null && !command.macAddress().isBlank()) {
            var deviceQuery = new com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByMacAddressQuery(
                    command.macAddress());
            var device = deviceQueryService.handle(deviceQuery);

            if (device.isPresent()) {
                var vehicleIdLong = device.get().getVehicleId();
                if (vehicleIdLong != null) {
                    var vehicleQuery = new com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery(
                            vehicleIdLong);
                    var vehicle = vehicleQueryService.handle(vehicleQuery);

                    if (vehicle.isPresent()) {
                        var vehicleId = new com.safecar.platform.workshop.domain.model.valueobjects.VehicleId(
                                vehicleIdLong);
                        var driverId = new com.safecar.platform.workshop.domain.model.valueobjects.DriverId(
                                vehicle.get().getDriverId());

                        // Reconstruimos el sample con los IDs resueltos
                        sample = new com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample(
                                sample.type(), sample.severity(), sample.timestamp(),
                                vehicleId, driverId,
                                sample.speed(), sample.location(), sample.odometer(), sample.dtc(),
                                sample.tirePressure(), sample.cabinGasLevel(), sample.accelerationVector(),
                                sample.engineTemperature(), sample.cabinTemperature(), sample.cabinHumidity(),
                                sample.electricalCurrent());
                    } else {
                        logger.warn("Vehicle not found for ID: {}", vehicleIdLong);
                    }
                } else {
                    logger.warn("Device {} is not assigned to any vehicle", command.macAddress());
                }
            } else {
                logger.warn("Device not found for MAC: {}", command.macAddress());
            }
        }

        logger.info("Processing telemetry ingestion command for vehicle: {}", sample.vehicleId());

        var finalSample = sample;

        try {
            // Validate that the vehicle exists in the Devices context before ingesting
            // telemetry
            if (finalSample.vehicleId() == null) {
                throw new IllegalArgumentException(
                        "Vehicle ID could not be resolved from metadata (MAC Address: " + command.macAddress() + ")");
            }

            externalDeviceService.validateVehicleExists(finalSample.vehicleId());
            logger.debug("Vehicle validation successful for vehicleId: {}", finalSample.vehicleId());

            var telemetry = vehicleTelemetryRepository.findByVehicleId(finalSample.vehicleId())
                    .orElseGet(() -> new VehicleTelemetry(finalSample.vehicleId()));

            telemetry.ingest(finalSample);
            vehicleTelemetryRepository.save(telemetry);

            logger.info("Successfully ingested telemetry record for vehicle: {}", finalSample.vehicleId());

        } catch (Exception e) {
            logger.error("Failed to ingest telemetry for vehicle: {} - Error: {}",
                    finalSample.vehicleId(), e.getMessage());
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
