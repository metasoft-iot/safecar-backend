package com.safecar.platform.workshop.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.safecar.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.commands.UpdateWorkshopCommand;
import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;
import com.safecar.platform.workshop.domain.model.queries.GetMechanicsByWorkshopIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByBusinessProfileIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.domain.services.MechanicQueryService;
import com.safecar.platform.workshop.domain.services.WorkshopCommandService;
import com.safecar.platform.workshop.domain.services.WorkshopQueryService;
import com.safecar.platform.workshop.interfaces.rest.resources.UpdateWorkshopResource;

@WebMvcTest(WorkshopsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WorkshopsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkshopCommandService workshopCommandService;

    @MockitoBean
    private WorkshopQueryService workshopQueryService;

    @MockitoBean
    private MechanicQueryService mechanicQueryService;

    @MockitoBean
    private ProfilesContextFacade profilesContextFacade;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllWorkshops_ReturnsWorkshops() throws Exception {
        // Arrange
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopQueryService.handle(any(GetAllWorkshopsQuery.class))).thenReturn(List.of(workshop));
        when(profilesContextFacade.getBusinessNameByProfileId(1L)).thenReturn("Business Name");
        when(profilesContextFacade.getBusinessAddressByProfileId(1L)).thenReturn("Address");

        // Act & Assert
        mockMvc.perform(get("/api/v1/workshops")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].workshopDescription").value("Description"));
    }

    @Test
    public void getWorkshopById_WhenWorkshopExists_ReturnsWorkshop() throws Exception {
        // Arrange
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopQueryService.handle(any(GetWorkshopByIdQuery.class))).thenReturn(Optional.of(workshop));
        when(profilesContextFacade.getBusinessNameByProfileId(1L)).thenReturn("Business Name");
        when(profilesContextFacade.getBusinessAddressByProfileId(1L)).thenReturn("Address");

        // Act & Assert
        mockMvc.perform(get("/api/v1/workshops/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.workshopDescription").value("Description"));
    }

    @Test
    public void getWorkshopById_WhenWorkshopNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(workshopQueryService.handle(any(GetWorkshopByIdQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/workshops/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateWorkshop_WhenWorkshopExists_ReturnsUpdatedWorkshop() throws Exception {
        // Arrange
        UpdateWorkshopResource resource = new UpdateWorkshopResource("Updated Description");
        Workshop workshop = new Workshop(1L, "Updated Description");
        workshop.setId(1L);

        when(workshopCommandService.handle(any(UpdateWorkshopCommand.class))).thenReturn(Optional.of(workshop));
        when(profilesContextFacade.getBusinessNameByProfileId(1L)).thenReturn("Business Name");
        when(profilesContextFacade.getBusinessAddressByProfileId(1L)).thenReturn("Address");

        // Act & Assert
        mockMvc.perform(patch("/api/v1/workshops/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workshopDescription").value("Updated Description"));
    }

    @Test
    public void getMechanicsByWorkshopId_ReturnsMechanics() throws Exception {
        // Arrange
        when(mechanicQueryService.handle(any(GetMechanicsByWorkshopIdQuery.class))).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/workshops/1/mechanics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getWorkshopByBusinessProfileId_WhenWorkshopExists_ReturnsWorkshop() throws Exception {
        // Arrange
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopQueryService.handle(any(GetWorkshopByBusinessProfileIdQuery.class)))
                .thenReturn(Optional.of(workshop));
        when(profilesContextFacade.getBusinessNameByProfileId(1L)).thenReturn("Business Name");
        when(profilesContextFacade.getBusinessAddressByProfileId(1L)).thenReturn("Address");

        // Act & Assert
        mockMvc.perform(get("/api/v1/workshops/by-business-profile/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
