package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.interfaces.rest.resources.DriverResource;

/**
 * Driver Resource From Entity Assembler
 * <p>
 * Transforms the {@link Driver} entity into a {@link DriverResource} for REST
 * responses.
 * </p>
 */
public class DriverResourceFromEntityAssembler {
    /**
     * Transforms the {@link Driver} entity into a {@link DriverResource}.
     * 
     * @param entity the entity to transform
     * @return the transformed {@link DriverResource}
     */
    public static DriverResource toResourceFromEntity(Driver entity) {
        return new DriverResource(
                entity.getDriverId(),
                entity.getTotalVehicles());
    }
}
