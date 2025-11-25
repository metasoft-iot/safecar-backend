package com.safecar.platform.insights.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.devices.interfaces.acl.DevicesContextFacade;
import com.safecar.platform.insights.application.internal.outboundservices.analytics.TelemetryAnalyticsGateway;
import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;
import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.VehicleInsightRepository;
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.workshop.domain.model.valueobjects.AlertSeverity;
import com.safecar.platform.workshop.domain.model.valueobjects.DriverId;
import com.safecar.platform.workshop.domain.model.valueobjects.GeoPoint;
import com.safecar.platform.workshop.domain.model.valueobjects.TelemetrySample;
import com.safecar.platform.workshop.domain.model.valueobjects.TelemetryTimestamp;

import com.safecar.platform.workshop.domain.model.valueobjects.VehicleId;
import com.safecar.platform.workshop.interfaces.acl.WorkshopContextFacade;

@ExtendWith(MockitoExtension.class)
public class VehicleInsightCommandServiceImplTest {

    @Mock
    private VehicleInsightRepository repository;

    @Mock
    private TelemetryAnalyticsGateway analyticsGateway;

    @Mock
    private WorkshopContextFacade workshopContextFacade;

    @Mock
    private DevicesContextFacade devicesContextFacade;

    @Mock
    private ProfilesContextFacade profilesContextFacade;

    @InjectMocks
    private VehicleInsightCommandServiceImpl commandService;

    @Test
    public void handleGenerateVehicleInsightCommand_WhenValidData_ReturnsInsight() {
        // Arrange
        GenerateVehicleInsightCommand command = new GenerateVehicleInsightCommand(1L);

        TelemetrySample sample = mock(TelemetrySample.class);
        VehicleId vehicleId = new VehicleId(1L);
        DriverId driverId = new DriverId(1L);
        TelemetryTimestamp timestamp = new TelemetryTimestamp(Instant.now());
        GeoPoint location = new GeoPoint(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0));

        when(sample.vehicleId()).thenReturn(vehicleId);
        when(sample.driverId()).thenReturn(driverId);
        when(sample.timestamp()).thenReturn(timestamp);
        when(sample.location()).thenReturn(location);
        when(sample.severity()).thenReturn(AlertSeverity.INFO);

        when(workshopContextFacade.fetchTelemetrySample(1L)).thenReturn(Optional.of(sample));
        when(devicesContextFacade.fetchVehicleLicensePlate(1L)).thenReturn("ABC-123");
        when(workshopContextFacade.fetchRecentTelemetrySamples(eq(1L), anyInt())).thenReturn(List.of(sample));

        TelemetryInsightResult analysisResult = new TelemetryInsightResult(
                "LOW", "Good", "Next month", 90, "Good habits", "None", null, "response_id", null);

        when(analyticsGateway.analyze(any(VehicleReference.class), anyList())).thenReturn(analysisResult);

        VehicleInsight insight = new VehicleInsight(
                new VehicleReference(1L, "Driver 1", 1L, "ABC-123"),
                analysisResult);

        when(repository.save(any(VehicleInsight.class))).thenReturn(insight);

        // Act
        VehicleInsight result = commandService.handle(command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRiskLevel()).isEqualTo("LOW");
    }
}
