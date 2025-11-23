package com.safecar.platform.profiles.domain.model.commands;

/**
 * Create Business Profile Command
 * 
 * @param businessName    the business name
 * @param ruc           the ruc number
 * @param businessAddress the business address
 * @param contactPhone    the contact phone number
 * @param contactEmail    the contact email
 */
public record CreateBusinessProfileCommand(
        String businessName,
        String ruc,
        String businessAddress,
        String contactPhone,
        String contactEmail
) {
}