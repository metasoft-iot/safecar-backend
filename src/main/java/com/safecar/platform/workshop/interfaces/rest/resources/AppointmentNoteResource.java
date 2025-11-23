package com.safecar.platform.workshop.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Appointment Note Resource - Represents a required appointment note.
 * 
 * @param id        the unique identifier of the appointment note
 * @param content   the content of the note
 * @param authorId  the ID of the author who created the note
 * @param createdAt the timestamp when the note was created
 * @param updatedAt the timestamp when the note was last updated
 */
public record AppointmentNoteResource(
                Long id,
                String content,
                Long authorId,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
