package com.safecar.platform.insights.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents a single actionable recommendation.
 */
@Embeddable
public record InsightRecommendation(

        @Column(name = "recommendation_title", length = 120)
        String title,

        @Column(name = "recommendation_detail", columnDefinition = "TEXT")
        String detail
) {
    public InsightRecommendation {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Recommendation title cannot be blank");
        }
        if (detail == null || detail.isBlank()) {
            throw new IllegalArgumentException("Recommendation detail cannot be blank");
        }
    }
}
