package com.safecar.platform.payments.domain.model.commands;

import com.safecar.platform.payments.domain.model.valueobjects.PlanType;

/**
 * Create Checkout Session Command - Command to create a Stripe checkout session
 * for a subscription payment.
 * 
 * @param userId The ID of the user creating the checkout session.
 * @param planType The subscription plan type.
 */
public record CreateCheckoutSessionCommand(
        String userId,
        PlanType planType
) {
}
