package com.safecar.platform.workshop.domain.model.aggregates;

import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.safecar.platform.workshop.domain.model.entities.TelemetryRecord;
import com.safecar.platform.workshop.domain.model.events.TelemetryFlushedEvent;
import com.safecar.platform.workshop.domain.model.events.TelemetrySampleIngestedEvent;
import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "vehicle_telemetry")
public class VehicleTelemetry extends AuditableAbstractAggregateRoot<VehicleTelemetry> {

    @Embedded
    @AttributeOverride(name = "vehicleId", column = @Column(name = "vehicle_id"))
    private com.safecar.platform.workshop.domain.model.valueobjects.VehicleId vehicleId;

    @Column(name = "last_ingested_at")
    private Instant lastIngestedAt;

    @Column(name = "record_count", nullable = false)
    private long recordCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "telemetry_aggregate_id")
    private final List<TelemetryRecord> records = new ArrayList<>();

    protected VehicleTelemetry() {
        this.recordCount = 0;
    }

    public VehicleTelemetry(com.safecar.platform.workshop.domain.model.valueobjects.VehicleId vehicleId) {
        this.vehicleId = vehicleId;
        this.recordCount = 0;
    }

    public void ingest(TelemetrySample sample) {
        if (sample == null)
            throw new IllegalArgumentException("Telemetry sample cannot be null");
        var now = Instant.now();
        var record = new TelemetryRecord(sample, now);
        this.records.add(record);
        this.recordCount = this.recordCount + 1;
        this.lastIngestedAt = now;
        registerEvent(new TelemetrySampleIngestedEvent(record.getId(), sample, now));
    }

    public long flush() {
        var count = this.records.size();
        this.records.clear();
        this.recordCount = 0;
        var flushedAt = Instant.now();
        registerEvent(new TelemetryFlushedEvent(this.getId(), flushedAt, count));
        return count;
    }
}
