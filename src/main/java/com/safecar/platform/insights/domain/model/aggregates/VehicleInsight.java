package com.safecar.platform.insights.domain.model.aggregates;

import com.safecar.platform.insights.domain.model.events.VehicleInsightGeneratedEvent;
import com.safecar.platform.insights.domain.model.valueobjects.*;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "vehicle_insights")
public class VehicleInsight extends AuditableAbstractAggregateRoot<VehicleInsight> {

    @Embedded
    private VehicleReference vehicle;

    @Embedded
    private MaintenancePrediction maintenancePrediction;

    @Embedded
    private DrivingHabitSummary drivingHabits;

    @Column(name = "risk_level", columnDefinition = "TEXT")
    private String riskLevel;

    @Column(name = "llm_response_id", length = 80)
    private String llmResponseId;

    @Column(name = "insight_generated_at", nullable = false)
    private Instant generatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_insight_recommendations", joinColumns = @JoinColumn(name = "insight_id"))
    private final List<InsightRecommendation> recommendations = new ArrayList<>();

    protected VehicleInsight() {
    }

    public VehicleInsight(VehicleReference vehicle, TelemetryInsightResult result) {
        this.vehicle = vehicle;
        applyResult(result);
        registerEvent(new VehicleInsightGeneratedEvent(
                this.getId(),
                vehicle.vehicleId(),
                vehicle.driverId(),
                this.riskLevel,
                this.generatedAt
        ));
    }

    public void refresh(TelemetryInsightResult result) {
        applyResult(result);
        registerEvent(new VehicleInsightGeneratedEvent(
                this.getId(),
                vehicle.vehicleId(),
                vehicle.driverId(),
                this.riskLevel,
                this.generatedAt
        ));
    }

    private void applyResult(TelemetryInsightResult result) {
        this.maintenancePrediction = new MaintenancePrediction(
                result.riskLevel(),
                result.maintenanceSummary(),
                result.maintenanceWindow()
        );
        this.drivingHabits = new DrivingHabitSummary(
                result.drivingHabitScore(),
                result.drivingHabitSummary(),
                result.drivingAlerts()
        );
        this.riskLevel = result.riskLevel();
        this.llmResponseId = result.llmResponseId();
        this.generatedAt = result.generatedAt() != null ? result.generatedAt() : Instant.now();
        this.recommendations.clear();
        if (result.recommendations() != null) {
            this.recommendations.addAll(result.recommendations());
        }
    }
}
