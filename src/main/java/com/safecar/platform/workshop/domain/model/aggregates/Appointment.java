package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.commands.CreateAppointmentCommand;
import com.safecar.platform.workshop.domain.model.entities.AppointmentNote;
import com.safecar.platform.workshop.domain.model.entities.ServiceType;
import com.safecar.platform.workshop.domain.model.events.*;
import com.safecar.platform.workshop.domain.model.valueobjects.*;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Workshop Appointment Aggregate - This aggregate Root represents an
 * appointment in the workshop operations context.
 * 
 * This appointment has its own identity (workshop/vehicle/driver) and includes
 * service type information and optional mechanic assignment.
 */
@Getter
@Entity
@Table(name = "workshop_appointments")
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {

    /**
     * Workshop Identifier - The workshop where this appointment is scheduled.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "workshopId", column = @Column(name = "workshop_id", nullable = false))
    })
    private WorkshopId workshopId;

    /**
     * Vehicle Identifier - The vehicle for which this appointment is scheduled.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vehicleId", column = @Column(name = "vehicle_id", nullable = false))
    })
    private VehicleId vehicleId;

    /**
     * Driver Identifier - The driver who owns the vehicle.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "driverId", column = @Column(name = "driver_id", nullable = false))
    })
    private DriverId driverId;

    /**
     * Service Type - The type of service requested for this appointment.
     * Follows the same pattern as Role in IAM bounded context.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    /**
     * Custom Service Description - Optional detailed description when service type is CUSTOM.
     */
    @Column(name = "custom_service_description", length = 500)
    private String customServiceDescription;

    /**
     * Mechanic Identifier - The mechanic assigned to this appointment (optional).
     * Nullable because not all appointments have an assigned mechanic immediately.
     */
    @Column(name = "mechanic_id")
    private Long mechanicId;

    /**
     * Appointment Status - This enum represents the various states an appointment
     * can be in.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    /**
     * Scheduled Service Slot - The time slot for which the appointment is
     * scheduled.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startAt", column = @Column(name = "scheduled_start_at")),
            @AttributeOverride(name = "endAt", column = @Column(name = "scheduled_end_at"))
    })
    private ServiceSlot scheduledAt;

    /**
     * List of notes - The notes associated with the appointment.
     */
    @OneToMany(mappedBy = "workshopAppointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppointmentNote> notes;

    /**
     * Default constructor for JPA.
     */
    protected Appointment() {
        this.status = AppointmentStatus.PENDING;
        this.notes = new ArrayList<>();
    }

    /**
     * Constructs an Appointment from a CreateAppointmentCommand.
     * 
     * @param command The command containing appointment details (workshop, vehicle, driver, slot, serviceType)
     */
    public Appointment(CreateAppointmentCommand command) {
        this();
        this.workshopId = command.workshopId();
        this.vehicleId = command.vehicleId();
        this.driverId = command.driverId();
        this.scheduledAt = command.slot();
        this.serviceType = ServiceType.validateServiceType(command.serviceType());
        this.customServiceDescription = command.customServiceDescription();

        registerEvent(new AppointmentCreatedEvent(
                this.getId(),
                this.workshopId,
                this.vehicleId,
                this.driverId,
                this.scheduledAt,
                null  // Removed workOrderId parameter
        ));
    }

    /**
     * Reschedules - This method reschedules the appointment to a new time slot.
     * 
     * @param slot The new service slot
     */
    public void reschedule(ServiceSlot slot) {
        if (slot == null)
            throw new IllegalArgumentException("Service slot cannot be null");

        if (AppointmentStatus.COMPLETED.equals(this.status) || AppointmentStatus.CANCELLED.equals(this.status))
            throw new IllegalStateException("Cannot reschedule completed or cancelled appointments");

        var oldSlot = this.scheduledAt;
        this.scheduledAt = slot;

        registerEvent(new AppointmentRescheduledEvent(this.getId(), oldSlot, slot));
    }

    /**
     * Cancels - This method cancels the appointment.
     */
    public void cancel() {
        if (AppointmentStatus.COMPLETED.equals(this.status))
            throw new IllegalStateException("Cannot cancel completed appointments");

        this.status = AppointmentStatus.CANCELLED;
        registerEvent(new AppointmentCanceledEvent(this.getId(), Instant.now()));
    }

    /**
     * Confirm - This method confirm the appointment.
     */
    public void confirm() {
        if (!AppointmentStatus.PENDING.equals(this.status))
            throw new IllegalStateException("Only pending appointments can be confirmed");
        this.status = AppointmentStatus.CONFIRMED;
    }

    /**
     * Start - This method starts the appointment service.
     */
    public void start() {
        if (!AppointmentStatus.CONFIRMED.equals(this.status))
            throw new IllegalStateException("Only confirmed appointments can be started");
        this.status = AppointmentStatus.IN_PROGRESS;
    }

    /**
     * Complete - This method complete the appointment.
     */
    public void complete() {
        if (!AppointmentStatus.IN_PROGRESS.equals(this.status))
            throw new IllegalStateException("Only in-progress appointments can be completed");
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Get Workshop ID - Gets the workshop ID associated with this appointment.
     * 
     * @return WorkshopId of this appointment
     */
    public WorkshopId getWorkshopId() {
        return this.workshopId;
    }

    /**
     * Get Vehicle ID - Gets the vehicle ID associated with this appointment.
     * 
     * @return VehicleId of this appointment
     */
    public VehicleId getVehicleId() {
        return this.vehicleId;
    }

    /**
     * Get Driver ID - Gets the driver ID associated with this appointment.
     * 
     * @return DriverId of this appointment
     */
    public DriverId getDriverId() {
        return this.driverId;
    }

    /**
     * Add Note - This method add notes to the current appointment.
     * 
     * @param content  The content of the note
     * @param authorId The ID of the author
     */
    public void addNote(String content, Long authorId) {
        if (content == null || content.trim().isEmpty())
            throw new IllegalArgumentException("Note content cannot be null or empty");
        if (authorId == null)
            throw new IllegalArgumentException("Author ID cannot be null");

        var note = new AppointmentNote(content, authorId, this);
        this.notes.add(note);
    }

    /**
     * Get Notes - Gets the list of notes for this appointment
     * 
     * @return List of appointment notes
     */
    public List<AppointmentNote> getNotes() {
        return new ArrayList<>(this.notes);
    }

    public boolean updateAppointmentStatus(AppointmentStatus newAppointmentStatus) {
        if (this.status == newAppointmentStatus) {
            return false;
        }

        if (newAppointmentStatus == AppointmentStatus.PENDING) {
            return false;
        }

        if (newAppointmentStatus == AppointmentStatus.CONFIRMED) {
            this.confirm();
            return true;
        }

        if (newAppointmentStatus == AppointmentStatus.IN_PROGRESS) {
            this.start();
            return true;
        }

        if (newAppointmentStatus == AppointmentStatus.COMPLETED) {
            this.complete();
            return true;
        }

        if (newAppointmentStatus == AppointmentStatus.CANCELLED) {
            this.cancel();
            return true;
        }

        return false;
    }

    /**
     * Assign Mechanic - Assigns a mechanic to this appointment.
     * 
     * @param mechanicId The ID of the mechanic to assign
     */
    public void assignMechanic(Long mechanicId) {
        if (mechanicId == null)
            throw new IllegalArgumentException("Mechanic ID cannot be null");

        if (AppointmentStatus.COMPLETED.equals(this.status) || AppointmentStatus.CANCELLED.equals(this.status))
            throw new IllegalStateException("Cannot assign mechanic to completed or cancelled appointments");

        this.mechanicId = mechanicId;
        registerEvent(new MechanicAssignedToAppointmentEvent(this.getId(), mechanicId));
    }

    /**
     * Unassign Mechanic - Removes the mechanic assignment from this appointment.
     */
    public void unassignMechanic() {
        if (this.mechanicId == null)
            throw new IllegalStateException("No mechanic is currently assigned to this appointment");

        if (AppointmentStatus.COMPLETED.equals(this.status))
            throw new IllegalStateException("Cannot unassign mechanic from completed appointments");

        var previousMechanicId = this.mechanicId;
        this.mechanicId = null;
        registerEvent(new MechanicUnassignedFromAppointmentEvent(this.getId(), previousMechanicId));
    }

}