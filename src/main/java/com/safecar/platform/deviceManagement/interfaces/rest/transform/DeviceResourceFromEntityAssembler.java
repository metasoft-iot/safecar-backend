package com.safecar.platform.deviceManagement.interfaces.rest.transform;

import com.safecar.platform.deviceManagement.domain.model.aggregates.Device;
import com.safecar.platform.deviceManagement.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {

    public static DeviceResource toResourceFromEntity(Device entity, String licensePlate) {
        return new DeviceResource(
                entity.getId().toString(), // UUID lógico -> String
                entity.getMacAddress(), // MAC física
                entity.getDeviceType().name(), // Enum -> String
                entity.getStatus(),
                licensePlate // Placa del vehículo
        );
    }
}
