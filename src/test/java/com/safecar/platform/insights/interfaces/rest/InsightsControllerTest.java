package com.safecar.platform.insights.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safecar.platform.insights.domain.model.aggregates.VehicleInsight;
import com.safecar.platform.insights.domain.model.commands.GenerateVehicleInsightCommand;
import com.safecar.platform.insights.domain.model.queries.GetVehicleInsightByVehicleIdQuery;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;
import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.domain.services.VehicleInsightCommandService;
import com.safecar.platform.insights.domain.services.VehicleInsightQueryService;

@WebMvcTest(InsightsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InsightsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleInsightCommandService commandService;

    @MockitoBean
    private VehicleInsightQueryService queryService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    public void generateInsight_WhenValidData_ReturnsInsight() throws Exception {
        // Arrange
        VehicleReference vehicleReference = new VehicleReference(1L, "John Doe", 1L, "ABC-123");
        TelemetryInsightResult result = new TelemetryInsightResult(
                "LOW", "Good", "Next month", 90, "Good habits", "None", null, "response_id", null);
        VehicleInsight insight = new VehicleInsight(vehicleReference, result);
        insight.setId(1L);

        when(commandService.handle(any(GenerateVehicleInsightCommand.class))).thenReturn(insight);

        // Act & Assert
        mockMvc.perform(post("/api/v1/insights/generate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.riskLevel").value("LOW"));
    }

    @Test
    public void getLatestInsight_WhenInsightExists_ReturnsInsight() throws Exception {
        // Arrange
        VehicleReference vehicleReference = new VehicleReference(1L, "John Doe", 1L, "ABC-123");
        TelemetryInsightResult result = new TelemetryInsightResult(
                "LOW", "Good", "Next month", 90, "Good habits", "None", null, "response_id", null);
        VehicleInsight insight = new VehicleInsight(vehicleReference, result);
        insight.setId(1L);

        when(queryService.handle(any(GetVehicleInsightByVehicleIdQuery.class))).thenReturn(Optional.of(insight));

        // Act & Assert
        mockMvc.perform(get("/api/v1/insights/vehicle/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("LOW"));
    }

    @Test
    public void getLatestInsight_WhenInsightNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(queryService.handle(any(GetVehicleInsightByVehicleIdQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/insights/vehicle/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
