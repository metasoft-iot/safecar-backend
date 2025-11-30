package com.safecar.platform.deviceManagement.domain.services;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.domain.model.commands.CreateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceMetricsCommand;

import com.safecar.platform.deviceManagement.domain.model.commands.DeleteDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceCommand;

import java.util.Optional;

public interface DeviceCommandService {
    Optional<Device> handle(CreateDeviceCommand command);

    // Update devuelve Optional por si no encuentra el device
    Optional<Device> handle(UpdateDeviceMetricsCommand command);

    Optional<Device> handle(UpdateDeviceCommand command);

    void handle(DeleteDeviceCommand command);
}
