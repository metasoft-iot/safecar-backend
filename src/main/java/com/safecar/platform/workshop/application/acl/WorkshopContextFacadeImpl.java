package com.safecar.platform.workshop.application.acl;

import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
