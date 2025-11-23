package com.safecar.platform.workshop.interfaces.rest.resources;

/**
 * Update Workshop Resource
 * Only allows updating the workshop description.
 * @param workshopDescription new description text
 */
public record UpdateWorkshopResource(String workshopDescription) {
}
