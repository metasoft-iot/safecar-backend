package com.safecar.platform.devices.domain.model.commands;

/**
 * Command to create a Driver in the Devices BC.
 * 
 * @param profileId the ID of the PersonProfile from Profiles BC
 */
public record CreateDriverCommand(Long profileId) {
}