package com.safecar.platform.payments.interfaces.rest.transform;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import com.safecar.platform.payments.interfaces.rest.resources.SubscriptionResource;

/**
 * Subscription Resource From Aggregate Assembler - Converts a Subscription aggregate
 * to a SubscriptionResource.
 */
public class SubscriptionResourceFromAggregateAssembler {

    /**
     * Converts a {@link Subscription} aggregate to a {@link SubscriptionResource}.
     *
     * @param subscription the subscription aggregate
     * @return the subscription resource
     */
    public static SubscriptionResource toResourceFromAggregate(Subscription subscription) {
        return new SubscriptionResource(
                subscription.getId().toString(),
                subscription.getUserId(),
                subscription.getPlanType().name(),
                subscription.getStatus(),
                subscription.getStripeSubscriptionId()
        );
    }
}
