package com.safecar.platform.profiles.interfaces.rest.resource;

public record UpdatePersonProfileResource(
        Long personId,
        String fullName,
        String city,
        String country,
        String phone,
        String dni) {

}
