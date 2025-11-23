package com.safecar.platform.payments.domain.model.commands;

import com.safecar.platform.payments.domain.model.valueobjects.PlanType;

/**
 * Create Subscription Command - Command to create a subscription record
 * after successful payment in Stripe.
 * 
 * @param userId The ID of the user.
 * @param stripeSubscriptionId The Stripe subscription ID.
 * @param planType The subscription plan type.
 */
public record CreateSubscriptionCommand(
        String userId,
        String stripeSubscriptionId,
        PlanType planType
) {
}
