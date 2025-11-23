package com.safecar.platform.profiles.interfaces.rest.resource;

/**
 * Update Business Resource
 * <p>
 * This record represents the data required to update a business profile.
 * </p>
 * 
 * @param businessName    - The name of the business
 * @param ruc             - The RUC (Registro Único de Contribuyentes) of the
 *                        business
 * @param businessAddress - The address of the business
 * @param contactPhone    - The contact phone number of the business
 * @param contactEmail    - The contact email of the business
 */
public record UpdateBusinessProfileResource(
        String businessName,
        String ruc,
        String businessAddress,
        String contactPhone,
        String contactEmail) {
}
