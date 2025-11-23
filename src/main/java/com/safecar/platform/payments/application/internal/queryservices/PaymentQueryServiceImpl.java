package com.safecar.platform.payments.application.internal.queryservices;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import com.safecar.platform.payments.domain.model.queries.GetSubscriptionByUserIdQuery;
import com.safecar.platform.payments.domain.services.PaymentQueryService;
import com.safecar.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Payment Query Service Implementation - Handles payment-related query operations.
 */
@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public PaymentQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByUserIdQuery query) {
        return subscriptionRepository.findByUserId(query.userId());
    }
}
