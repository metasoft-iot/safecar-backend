package com.safecar.platform.payments.domain.services;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import com.safecar.platform.payments.domain.model.queries.GetSubscriptionByUserIdQuery;

import java.util.Optional;

/**
 * Payment Query Service - Domain service interface for payment-related queries.
 * Handles retrieval of subscription information.
 */
public interface PaymentQueryService {

    /**
     * Get subscription by user ID.
     * 
     * @param query the get subscription by user ID query
     * @return the subscription if found
     */
    Optional<Subscription> handle(GetSubscriptionByUserIdQuery query);
}
