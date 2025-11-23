package com.safecar.platform.insights.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Captures the feedback about driver behaviour analysed by the LLM.
 */
@Embeddable
public record DrivingHabitSummary(

        @Column(name = "driving_habit_score")
        Integer habitScore,

        @Column(name = "driving_habit_summary", columnDefinition = "TEXT")
        String summary,

        @Column(name = "driving_alerts", columnDefinition = "TEXT")
        String alerts
) {
    public DrivingHabitSummary {
        if (habitScore != null && (habitScore < 0 || habitScore > 100)) {
            throw new IllegalArgumentException("Habit score must be between 0 and 100");
        }
    }
}
