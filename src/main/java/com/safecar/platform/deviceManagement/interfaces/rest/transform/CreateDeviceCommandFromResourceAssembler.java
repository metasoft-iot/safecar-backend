package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.commands.CreateDeviceCommand;
import com.safecar.platform.deviceManagement.domain.model.valueobjects.DeviceType;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.CreateDeviceResource;

public class CreateDeviceCommandFromResourceAssembler {

    public static CreateDeviceCommand toCommandFromResource(CreateDeviceResource resource) {
        return new CreateDeviceCommand(
                resource.macAddress(),
                DeviceType.valueOf(resource.deviceType().toUpperCase()), // String -> Enum
                resource.licensePlate());
    }
}
