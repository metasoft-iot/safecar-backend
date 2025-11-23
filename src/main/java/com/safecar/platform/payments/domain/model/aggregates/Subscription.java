package com.safecar.platform.payments.domain.model.aggregates;

import com.safecar.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.safecar.platform.payments.domain.model.valueobjects.PlanType;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * Subscription Aggregate - Represents a subscription to a plan in the system.
 * This aggregate manages the lifecycle of a subscription including creation,
 * activation, and cancellation.
 */
@Getter
@Entity
@Table(name = "subscriptions")
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {

    /**
     * User ID associated with this subscription
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Subscription plan type
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlanType planType;

    /**
     * Subscription status (ACTIVE, CANCELLED, EXPIRED, etc.)
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * Stripe subscription ID for external reference
     */
    @Column(name = "stripe_subscription_id", unique = true)
    private String stripeSubscriptionId;

    /**
     * Default constructor for JPA
     */
    protected Subscription() {
        super();
        this.status = "ACTIVE";
    }

    /**
     * Constructor from CreateSubscriptionCommand
     * 
     * @param command the create subscription command
     */
    public Subscription(CreateSubscriptionCommand command) {
        this();
        this.userId = command.userId();
        this.planType = command.planType();
        this.stripeSubscriptionId = command.stripeSubscriptionId();
    }

    /**
     * Constructor with individual parameters
     * 
     * @param userId the user ID
     * @param planType the plan type
     * @param stripeSubscriptionId the Stripe subscription ID
     */
    public Subscription(String userId, PlanType planType, String stripeSubscriptionId) {
        this();
        this.userId = userId;
        this.planType = planType;
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    /**
     * Cancel the subscription
     */
    public void cancel() {
        this.status = "CANCELLED";
    }

    /**
     * Activate the subscription
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * Expire the subscription
     */
    public void expire() {
        this.status = "EXPIRED";
    }

    /**
     * Check if subscription is active
     * 
     * @return true if subscription is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Get the mechanics limit for this subscription plan
     * 
     * @return the mechanics limit
     */
    public int getMechanicsLimit() {
        return this.planType.getMechanicsLimit();
    }
}
