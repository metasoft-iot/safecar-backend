package com.safecar.platform.profiles.interfaces.rest.resource;

import java.util.Set;

/**
 * PersonProfile Resource
 * <p>
 * Represents a person's profile information in the REST API.
 * </p>
 * 
 * @param profileId         Unique identifier for the profile.
 * @param userEmail         Email of the user associated with this profile.
 * @param fullName          Full name of the person.
 * @param city              City where the person resides.
 * @param country           Country where the person resides.
 * @param phone             Contact phone number of the person.
 * @param dni               National identification number of the person.
 * @param companyName       Name of the company (if the person is a mechanic).
 * @param specializations   Set of specializations (if the person is a
 *                          mechanic).
 * @param yearsOfExperience Years of experience (if the person is a mechanic).
 */
public record PersonProfileResource(
                Long profileId,
                String userEmail,
                String fullName,
                String city,
                String country,
                String phone,
                String dni,
                String companyName,
                Set<String> specializations,
                Integer yearsOfExperience) {
}
