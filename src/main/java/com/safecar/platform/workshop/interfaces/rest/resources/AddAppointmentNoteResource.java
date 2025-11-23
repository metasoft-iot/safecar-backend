package com.safecar.platform.workshop.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Add Appointment Note Resource - Represents the data required to add a note to
 * an appointment.
 * 
 * @param content  the content of the note
 * @param authorId the ID of the author adding the note
 */
public record AddAppointmentNoteResource(
        @NotBlank(message = "Content is required") String content,
        @NotNull(message = "Author ID is required") Long authorId) {
}
