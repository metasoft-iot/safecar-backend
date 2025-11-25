package com.safecar.platform.devices.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecar.platform.devices.domain.model.aggregates.Vehicle;
import com.safecar.platform.devices.domain.model.commands.CreateVehicleCommand;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByDriverIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehiclesByWorkshopIdQuery;
import com.safecar.platform.devices.domain.services.VehicleCommandService;
import com.safecar.platform.devices.domain.services.VehicleQueryService;
import com.safecar.platform.devices.interfaces.rest.resources.CreateVehicleResource;

@WebMvcTest(VehiclesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VehiclesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleCommandService vehicleCommandService;

    @MockitoBean
    private VehicleQueryService vehicleQueryService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createVehicle_WhenValidData_ReturnsCreatedVehicle() throws Exception {
        // Arrange
        CreateVehicleResource resource = new CreateVehicleResource(1L, "ABC-123", "Brand", "Model");
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model");
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        when(vehicleCommandService.handle(any(CreateVehicleCommand.class))).thenReturn(Optional.of(vehicle));

        // Act & Assert
        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));
    }

    @Test
    public void getVehicleById_WhenVehicleExists_ReturnsVehicle() throws Exception {
        // Arrange
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model");
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        when(vehicleQueryService.handle(any(GetVehicleByIdQuery.class))).thenReturn(Optional.of(vehicle));

        // Act & Assert
        mockMvc.perform(get("/api/v1/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));
    }

    @Test
    public void getVehicleById_WhenVehicleNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(vehicleQueryService.handle(any(GetVehicleByIdQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getVehiclesByDriverId_WhenVehiclesExist_ReturnsVehicles() throws Exception {
        // Arrange
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model");
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        when(vehicleQueryService.handle(any(GetVehicleByDriverIdQuery.class))).thenReturn(List.of(vehicle));

        // Act & Assert
        mockMvc.perform(get("/api/v1/drivers/1/vehicles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void getVehiclesByWorkshopId_WhenVehiclesExist_ReturnsVehicles() throws Exception {
        // Arrange
        CreateVehicleCommand command = new CreateVehicleCommand(1L, "ABC-123", "Brand", "Model");
        Vehicle vehicle = new Vehicle(command);
        vehicle.setId(1L);

        when(vehicleQueryService.handle(any(GetVehiclesByWorkshopIdQuery.class))).thenReturn(List.of(vehicle));

        // Act & Assert
        mockMvc.perform(get("/api/v1/workshops/1/vehicles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
