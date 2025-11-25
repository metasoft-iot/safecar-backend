package com.safecar.platform.devices.interfaces.rest;

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

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.queries.GetDriverByProfileIdQuery;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.devices.domain.services.DriverQueryService;

@WebMvcTest(DriversController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DriversControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DriverQueryService driverQueryService;

    @MockitoBean
    private DriverCommandService driverCommandService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    public void getDriverByProfileId_WhenDriverExists_ReturnsDriver() throws Exception {
        Driver driver = new Driver(1L);
        driver.setId(1L);

        when(driverQueryService.handle(any(GetDriverByProfileIdQuery.class))).thenReturn(Optional.of(driver));

        mockMvc.perform(get("/api/v1/profiles/1/driver")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(1L))
                .andExpect(jsonPath("$.totalVehicles").value(0));
    }

    @Test
    public void getDriverByProfileId_WhenDriverNotFound_ReturnsNotFound() throws Exception {
        when(driverQueryService.handle(any(GetDriverByProfileIdQuery.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/profiles/1/driver")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDriverForProfile_WhenValidData_ReturnsCreatedDriver() throws Exception {
        Driver driver = new Driver(1L);
        driver.setId(1L);

        when(driverCommandService.handle(any(CreateDriverCommand.class))).thenReturn(Optional.of(driver));

        mockMvc.perform(post("/api/v1/profiles/1/driver")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.driverId").value(1L))
                .andExpect(jsonPath("$.totalVehicles").value(0));
    }

    @Test
    public void createDriverForProfile_WhenDriverAlreadyExists_ReturnsConflict() throws Exception {
        when(driverCommandService.handle(any(CreateDriverCommand.class)))
                .thenThrow(new IllegalArgumentException("Driver already exists"));

        mockMvc.perform(post("/api/v1/profiles/1/driver")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
