package com.safecar.platform.profiles.domain.model.commands;

/**
 * Create Business Profile Command
 * 
 * @param username        the username or nickname
 * @param businessName    the business name
 * @param ruc             the ruc number
 * @param businessAddress the business address
 * @param contactPhone    the contact phone number
 * @param contactEmail    the contact email
 */
public record CreateBusinessProfileCommand(
        String username,
        String businessName,
        String ruc,
        String businessAddress,
        String contactPhone,
        String contactEmail,
        String description) {
}