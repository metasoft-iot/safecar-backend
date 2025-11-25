package com.safecar.platform.profiles.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdateBusinessProfileCommand;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.services.BusinessProfileCommandService;
import com.safecar.platform.profiles.domain.services.BusinessProfileQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.CreateBusinessProfileResource;
import com.safecar.platform.profiles.interfaces.rest.resource.UpdateBusinessProfileResource;

@WebMvcTest(BusinessProfilesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BusinessProfilesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BusinessProfileQueryService businessProfileQueryService;

    @MockitoBean
    private BusinessProfileCommandService businessProfileCommandService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getBusinessProfileByEmail_WhenProfileExists_ReturnsProfile() throws Exception {
        // Arrange
        BusinessProfile profile = new BusinessProfile("test@example.com", "Business Name", "12345678901", "Address",
                "123456789", "contact@example.com", "Description");
        profile.setId(1L);

        when(businessProfileQueryService.handle(any(GetBusinessProfileByUserEmailQuery.class)))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(get("/api/v1/business-profiles")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.businessName").value("Business Name"));
    }

    @Test
    public void getBusinessProfileByEmail_WhenProfileNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(businessProfileQueryService.handle(any(GetBusinessProfileByUserEmailQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/business-profiles")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNewBusinessProfile_WhenValidData_ReturnsCreatedProfile() throws Exception {
        // Arrange
        CreateBusinessProfileResource resource = new CreateBusinessProfileResource("Business Name", "12345678901",
                "Address", "123456789", "contact@example.com", "Description");
        BusinessProfile profile = new BusinessProfile("test@example.com", "Business Name", "12345678901", "Address",
                "123456789", "contact@example.com", "Description");
        profile.setId(1L);

        when(businessProfileCommandService.handle(any(CreateBusinessProfileCommand.class), anyString()))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(post("/api/v1/business-profiles")
                .param("userEmail", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.businessName").value("Business Name"));
    }

    @Test
    public void updateBusinessProfile_WhenValidData_ReturnsUpdatedProfile() throws Exception {
        // Arrange
        UpdateBusinessProfileResource resource = new UpdateBusinessProfileResource("Business Name Updated",
                "12345678901", "Address", "123456789", "contact@example.com", "Description");
        BusinessProfile profile = new BusinessProfile("test@example.com", "Business Name Updated", "12345678901",
                "Address", "123456789", "contact@example.com", "Description");
        profile.setId(1L);

        when(businessProfileCommandService.handle(any(UpdateBusinessProfileCommand.class), anyLong()))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(put("/api/v1/business-profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.businessName").value("Business Name Updated"));
    }
}
