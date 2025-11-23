package com.safecar.platform.workshop.domain.model.commands;

import java.util.Set;

import com.safecar.platform.workshop.domain.model.entities.Specialization;

/**
 * Command to create a new mechanic.
 * 
 * @param profileId the profile id from Profiles BC
 * @param workshopId the workshop id to which this mechanic belongs
 * @param specializations the specializations of the mechanic
 * @param yearsOfExperience the years of experience
 */
public record CreateMechanicCommand(
        Long profileId,
        Long workshopId,
        Set<Specialization> specializations,
        Integer yearsOfExperience) {
}