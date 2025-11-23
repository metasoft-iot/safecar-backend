package com.safecar.platform.workshop.application.acl;

import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;
import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Workshop Context Facade Implementation
 * 
 * <p>
 * This class implements the {@link WorkshopContextFacade} interface.
 * </p>
 * 
 * Methods:
 * <ul>
 * <li>{@link WorkshopContextFacade#fetchTelemetrySample(Long)}</li>
 * <li>{@link WorkshopContextFacade#fetchRecentTelemetrySamples(String, int)}</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class WorkshopContextFacadeImpl implements WorkshopContextFacade {

    private final TelemetryRecordRepository telemetryRecordRepository;

    // {@inheritDoc}
    @Override
    public Optional<TelemetrySample> fetchTelemetrySample(Long telemetryId) {
        return telemetryRecordRepository.findById(telemetryId)
                .map(record -> record.getSample());
    }

    // {@inheritDoc}
    @Override
    public List<TelemetrySample> fetchRecentTelemetrySamples(Long vehicleId, int limit) {
        return telemetryRecordRepository.findBySampleVehicleIdOrderByIngestedAtDesc(new VehicleId(vehicleId))
                .stream()
                .limit(limit)
                .map(record -> record.getSample())
                .toList();
    }
}
