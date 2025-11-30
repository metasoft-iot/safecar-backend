package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.commands.UpdateDeviceCommand;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.UpdateDeviceResource;

public class UpdateDeviceCommandFromResourceAssembler {
    public static UpdateDeviceCommand toCommandFromResource(Long deviceId, UpdateDeviceResource resource) {
        return new UpdateDeviceCommand(
                deviceId,
                resource.licensePlate(),
                resource.status());
    }
}
