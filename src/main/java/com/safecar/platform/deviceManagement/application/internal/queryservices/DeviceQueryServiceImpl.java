package com.safecar.platform.deviceManagement.application.internal.queryservices;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByIdQuery;
import com.safecar.platform.deviceManagement.domain.model.queries.GetDeviceByMacAddressQuery;
import com.safecar.platform.deviceManagement.domain.services.DeviceQueryService;
import com.safecar.platform.deviceManagement.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceByMacAddressQuery query) {
        return deviceRepository.findByMacAddress(query.macAddress());
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        try {
            // Convertimos String a UUID para el repositorio
            UUID id = UUID.fromString(query.deviceId());
            return deviceRepository.findById(id);
        } catch (IllegalArgumentException e) {
            // Si el ID no es un UUID válido
            return Optional.empty();
        }
    }
}