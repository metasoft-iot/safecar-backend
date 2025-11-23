package com.safecar.platform.profiles.interfaces.rest.resource;

public record CreateBusinessProfileResource(
        String businessName,
        String ruc,
        String businessAddress,
        String contactPhone,
        String contactEmail) {
}
