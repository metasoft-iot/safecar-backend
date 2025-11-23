package com.safecar.platform.profiles.interfaces.rest.transform;

import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.interfaces.rest.resource.BusinessProfileResource;

public class BusinessProfileResourceFromEntityAssembler {
    public static BusinessProfileResource toResourceFromEntity(BusinessProfile entity) {
        return new BusinessProfileResource(
                entity.getId(),
                entity.getBusinessName(),
                entity.getRuc(),
                entity.getBusinessAddress(),
                entity.getContactPhone(),
                entity.getContactEmail());
    }
}
