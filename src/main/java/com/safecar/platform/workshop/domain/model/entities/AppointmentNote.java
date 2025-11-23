package com.safecar.platform.workshop.domain.model.entities;

import com.safecar.platform.shared.domain.model.entities.AuditableModel;
import com.safecar.platform.workshop.domain.model.aggregates.Appointment;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "workshop_appointment_notes")
public class AppointmentNote extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_appointment_id", nullable = false)
    private Appointment workshopAppointment;

    protected AppointmentNote() {
    }

    public AppointmentNote(String content, Long authorId, Appointment workshopAppointment) {
        this.content = content;
        this.authorId = authorId;
        this.workshopAppointment = workshopAppointment;
    }
}


