package com.safecar.platform.payments.infrastructure.persistence.jpa.repositories;

import com.safecar.platform.payments.domain.model.aggregates.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Subscription Repository - JPA repository for Subscription aggregate.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Find subscription by user ID.
     * 
     * @param userId the user ID
     * @return optional subscription
     */
    Optional<Subscription> findByUserId(String userId);

    /**
     * Find subscription by Stripe subscription ID.
     * 
     * @param stripeSubscriptionId the Stripe subscription ID
     * @return optional subscription
     */
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    /**
     * Check if user has an active subscription.
     * 
     * @param userId the user ID
     * @param status the status to check
     * @return true if exists
     */
    boolean existsByUserIdAndStatus(String userId, String status);
}
