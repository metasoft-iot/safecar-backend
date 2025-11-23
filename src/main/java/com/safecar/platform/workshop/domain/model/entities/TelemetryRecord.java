package com.safecar.platform.workshop.domain.model.entities;

import com.safecar.platform.shared.domain.model.entities.AuditableModel;
import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;

@Getter
@Entity
@Table(name = "telemetry_records")
public class TelemetryRecord extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TelemetrySample sample;

    @Column(name = "telemetry_aggregate_id", nullable = true)
    private Long telemetryAggregateId;

    @Column(name = "ingested_at", nullable = false)
    private Instant ingestedAt;

    protected TelemetryRecord() {
    }

    public TelemetryRecord(TelemetrySample sample, Instant ingestedAt) {
        this.sample = sample;
        this.ingestedAt = ingestedAt;
    }

    public TelemetryRecord(Long telemetryAggregateId, TelemetrySample sample, Instant ingestedAt) {
        this.telemetryAggregateId = telemetryAggregateId;
        this.sample = sample;
        this.ingestedAt = ingestedAt;
    }

    public void setTelemetryAggregateId(Long telemetryAggregateId) {
        this.telemetryAggregateId = telemetryAggregateId;
    }
}
