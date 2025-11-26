package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Business Profile Resource
 * <p>
 * Represents the business profile information.
 * </p>
 * 
 * @param id              The id of the business.
 * @param username        The username or nickname of the account owner.
 * @param businessName    The name of the business.
 * @param ruc             The ruc of the business
 * @param businessAddress The address of the business.
 * @param contactPhone    The contact phone number for the business.
 * @param contactEmail    The contact email address for the business.
 */
public record BusinessProfileResource(
        Long id,
        String username,
        String businessName,
        String ruc,
        String businessAddress,
        String contactPhone,
        String contactEmail,
        String description) {
}
