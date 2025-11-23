package com.safecar.platform.workshop.application.internal.eventhandlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.safecar.platform.workshop.domain.model.events.AppointmentCanceledEvent;
import com.safecar.platform.workshop.domain.model.events.AppointmentCreatedEvent;
import com.safecar.platform.workshop.domain.model.events.AppointmentRescheduledEvent;

import java.sql.Timestamp;

/**
 * Event handler for Workshop Appointment related events.
 * <p>
 *     These events are triggered when appointments are created, canceled or rescheduled.
 *     Used for logging and potential integration with external systems.
 * </p>
 */
@Service
public class WorkshopAppointmentEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkshopAppointmentEventHandler.class);

    /**
     * Event listener for AppointmentCreatedEvent.
     * <p>
     *     This method is triggered when a new appointment is created.
     * </p>
     *
     * @param event the {@link AppointmentCreatedEvent} event.
     */
    @EventListener
    public void on(AppointmentCreatedEvent event) {
        LOGGER.info("Appointment created with ID {} for workshop {} at {}", 
                   event.appointmentId(), event.workshopId(), currentTimestamp());
        
        // Business logic: Track appointment creation metrics, validate workshop capacity
        LOGGER.debug("Appointment details - Vehicle: {}, Driver: {}, Scheduled: {} - {}", 
                    event.vehicleId(), event.driverId(), event.slot().startAt(), event.slot().endAt());
        
        // Additional business logic that can be triggered by events
        // Note: Primary notifications are sent from command services to ensure transactional consistency
        
        // TODO: Integrate with Analytics BC for appointment creation metrics
        // TODO: Integrate with Calendar BC for external calendar synchronization  
        // TODO: Integrate with Resource BC for workshop capacity tracking
    }

    /**
     * Event listener for AppointmentCanceledEvent.
     * <p>
     *     This method is triggered when an appointment is canceled.
     * </p>
     *
     * @param event the {@link AppointmentCanceledEvent} event.
     */
    @EventListener
    public void on(AppointmentCanceledEvent event) {
        LOGGER.info("Appointment with ID {} canceled at {}", 
                   event.appointmentId(), event.canceledAt());
        
        // Business logic: Free up resources and trigger compensating actions
        LOGGER.warn("APPOINTMENT CANCELLED - Workshop capacity freed, notification required for appointment ID: {}", 
                   event.appointmentId());
        
        // TODO: Integrate with Workshop BC to free up allocated resources/slots
        // TODO: Integrate with Notification BC for cancellation alerts to customer/mechanic
        // TODO: Integrate with Billing BC for cancellation fee processing
    }

    /**
     * Event listener for AppointmentRescheduledEvent.
     * <p>
     *     This method is triggered when an appointment is rescheduled.
     * </p>
     *
     * @param event the {@link AppointmentRescheduledEvent} event.
     */
    @EventListener
    public void on(AppointmentRescheduledEvent event) {
        LOGGER.info("Appointment with ID {} rescheduled from {} to {} at {}", 
                   event.appointmentId(), event.oldSlot(), event.newSlot(), currentTimestamp());
        
        // Business logic: Validate new slot availability and update external systems
        LOGGER.info("SCHEDULE CHANGE - Old slot freed: {} - {}, New slot allocated: {} - {}", 
                   event.oldSlot().startAt(), event.oldSlot().endAt(),
                   event.newSlot().startAt(), event.newSlot().endAt());
        
        // TODO: Integrate with Workshop BC to validate new slot availability  
        // TODO: Integrate with Notification BC for schedule change alerts
        // TODO: Integrate with Calendar BC for calendar updates
    }

    /**
     * Get the current timestamp.
     *
     * @return the current timestamp.
     */
    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}