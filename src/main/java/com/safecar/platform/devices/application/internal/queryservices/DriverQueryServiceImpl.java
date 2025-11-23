package com.safecar.platform.devices.application.internal.queryservices;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.queries.GetDriverByProfileIdQuery;
import com.safecar.platform.devices.domain.services.DriverQueryService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverQueryServiceImpl implements DriverQueryService {
    private final DriverRepository driverRepository;

    public DriverQueryServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> handle(GetDriverByProfileIdQuery query) {
        return driverRepository.findByProfileId_ProfileId(query.profileId());
    }
}