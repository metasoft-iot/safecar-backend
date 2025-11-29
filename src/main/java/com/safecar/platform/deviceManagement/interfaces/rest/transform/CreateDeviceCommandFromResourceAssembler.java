package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.commands.CreateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.valueobjects.DeviceType;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.CreateDeviceResource;

import java.util.UUID;

public class CreateDeviceCommandFromResourceAssembler {

    public static CreateDeviceCommand toCommandFromResource(CreateDeviceResource resource) {
        // Convertimos el String del vehículo a UUID de forma segura
        UUID vehicleUuid = null;
        if (resource.vehicleId() != null && !resource.vehicleId().isBlank()) {
            vehicleUuid = UUID.fromString(resource.vehicleId());
        }

        return new CreateDeviceCommand(
                resource.macAddress(),
                DeviceType.valueOf(resource.deviceType()), // String -> Enum
                vehicleUuid
        );
    }
}