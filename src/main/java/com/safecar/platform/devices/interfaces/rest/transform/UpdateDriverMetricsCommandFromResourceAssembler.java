package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.commands.UpdateNumberOfDriverVehiclesCommand;
import com.safecar.platform.devices.interfaces.rest.resources.UpdateDriverMetricsResource;

public class UpdateDriverMetricsCommandFromResourceAssembler {

    public static UpdateNumberOfDriverVehiclesCommand toCommandFromResource(UpdateDriverMetricsResource resource) {
        return new UpdateNumberOfDriverVehiclesCommand(
                resource.driverId());
    }
}
