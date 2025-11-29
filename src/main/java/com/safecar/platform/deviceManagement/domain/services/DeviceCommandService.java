package com.safecar.platform.deviceManagement.domain.services;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.domain.model.commands.CreateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceMetricsCommand;

import java.util.Optional;

public interface DeviceCommandService {
    Optional<Device> handle(CreateDeviceCommand command);

    // Update devuelve Optional por si no encuentra el device
    Optional<Device> handle(UpdateDeviceMetricsCommand command);
}