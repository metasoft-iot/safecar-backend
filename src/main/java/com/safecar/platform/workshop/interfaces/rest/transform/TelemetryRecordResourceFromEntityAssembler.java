package com.safecar.platform.workshop.interfaces.rest.transform;

import com.safecar.platform.workshop.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshop.interfaces.rest.resources.TelemetryRecordResource;

/**
 * Assembler to convert TelemetryRecord entity to TelemetryRecordResource.
 */
public class TelemetryRecordResourceFromEntityAssembler {

    public static TelemetryRecordResource toResourceFromEntity(TelemetryRecord entity) {
        if (entity == null) return null;
        return new TelemetryRecordResource(entity.getId(), entity.getSample(), entity.getIngestedAt());
    }
}
