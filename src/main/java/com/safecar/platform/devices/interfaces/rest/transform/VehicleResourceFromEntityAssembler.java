package com.safecar.platform.devices.interfaces.rest.transform;

import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.interfaces.rest.resources.VehicleResource;

/**
 * Assembler to convert Vehicle entity to Vehicle Resource.
 * <p>
 * This class provides a method to perform the transformation between the
 * entity representation and the resource representation.
 * </p>
 */
public class VehicleResourceFromEntityAssembler {
    /**
     * Transforms a Vehicle entity into a VehicleResource.
     * 
     * @param vehicle the Vehicle entity to be transformed
     * @return the corresponding VehicleResource
     */
    public static VehicleResource toResourceFromEntity(Vehicle vehicle) {
        return new VehicleResource(
                vehicle.getId(),
                vehicle.getDriverId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel());
    }
}
