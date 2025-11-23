package com.safecar.platform.payments.domain.services;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import com.safecar.platform.payments.domain.model.commands.CreateCheckoutSessionCommand;
import com.safecar.platform.payments.domain.model.commands.CreateSubscriptionCommand;

import java.util.Optional;

/**
 * Payment Command Service - Domain service interface for payment-related commands.
 * Handles creation of checkout sessions and subscription management.
 */
public interface PaymentCommandService {

    /**
     * Create a Stripe checkout session for a subscription.
     * 
     * @param command the create checkout session command
     * @return the Stripe session ID
     */
    String handle(CreateCheckoutSessionCommand command);

    /**
     * Create a subscription record after successful payment.
     * 
     * @param command the create subscription command
     * @return the created subscription
     */
    Optional<Subscription> handle(CreateSubscriptionCommand command);
}
