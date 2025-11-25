package com.safecar.platform.insights.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.queries.GetVehicleInsightByVehicleIdQuery;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;
import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.infrastructure.persistence.jpa.repositories.VehicleInsightRepository;

@ExtendWith(MockitoExtension.class)
public class VehicleInsightQueryServiceImplTest {

    @Mock
    private VehicleInsightRepository repository;

    @InjectMocks
    private VehicleInsightQueryServiceImpl queryService;

    @Test
    public void handleGetVehicleInsightByVehicleIdQuery_WhenInsightExists_ReturnsInsight() {
        // Arrange
        GetVehicleInsightByVehicleIdQuery query = new GetVehicleInsightByVehicleIdQuery(1L);

        VehicleReference vehicleReference = new VehicleReference(1L, "John Doe", 1L, "ABC-123");
        TelemetryInsightResult result = new TelemetryInsightResult(
                "LOW", "Good", "Next month", 90, "Good habits", "None", null, "response_id", null);
        VehicleInsight insight = new VehicleInsight(vehicleReference, result);
        insight.setId(1L);

        when(repository.findTopByVehicleVehicleIdOrderByGeneratedAtDesc(1L)).thenReturn(Optional.of(insight));

        // Act
        Optional<VehicleInsight> resultInsight = queryService.handle(query);

        // Assert
        assertThat(resultInsight).isPresent();
        assertThat(resultInsight.get().getId()).isEqualTo(1L);
    }

    @Test
    public void handleGetVehicleInsightByVehicleIdQuery_WhenInsightDoesNotExist_ReturnsEmpty() {
        // Arrange
        GetVehicleInsightByVehicleIdQuery query = new GetVehicleInsightByVehicleIdQuery(1L);

        when(repository.findTopByVehicleVehicleIdOrderByGeneratedAtDesc(1L)).thenReturn(Optional.empty());

        // Act
        Optional<VehicleInsight> resultInsight = queryService.handle(query);

        // Assert
        assertThat(resultInsight).isEmpty();
    }
}
