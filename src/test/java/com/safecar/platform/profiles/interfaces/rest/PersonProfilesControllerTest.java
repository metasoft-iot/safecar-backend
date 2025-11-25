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
import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.commands.UpdatePersonProfileCommand;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.domain.services.PersonProfileCommandService;
import com.safecar.platform.profiles.domain.services.PersonProfileQueryService;
import com.safecar.platform.profiles.interfaces.rest.resource.CreatePersonProfileResource;
import com.safecar.platform.profiles.interfaces.rest.resource.UpdatePersonProfileResource;

@WebMvcTest(PersonProfilesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PersonProfilesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonProfileQueryService personProfileQueryService;

    @MockitoBean
    private PersonProfileCommandService personProfileCommandService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getProfileById_WhenProfileExists_ReturnsProfile() throws Exception {
        // Arrange
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(personProfileQueryService.handle(any(GetPersonProfileByIdQuery.class))).thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(get("/api/v1/person-profiles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    public void getProfileById_WhenProfileNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(personProfileQueryService.handle(any(GetPersonProfileByIdQuery.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/person-profiles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProfileByUserEmail_WhenProfileExists_ReturnsProfile() throws Exception {
        // Arrange
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(personProfileQueryService.handle(any(GetPersonProfileByUserEmailQuery.class)))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(get("/api/v1/person-profiles")
                .param("userEmail", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(1L))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    public void createNewPersonProfile_WhenValidData_ReturnsCreatedProfile() throws Exception {
        // Arrange
        CreatePersonProfileResource resource = new CreatePersonProfileResource("John Doe", "City", "Country",
                "123456789", "12345678");
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(personProfileCommandService.handle(any(CreatePersonProfileCommand.class), anyString()))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(post("/api/v1/person-profiles")
                .param("userEmail", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.profileId").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    public void updatePersonProfile_WhenValidData_ReturnsUpdatedProfile() throws Exception {
        // Arrange
        UpdatePersonProfileResource resource = new UpdatePersonProfileResource(1L, "John Doe Updated", "City",
                "Country", "123456789", "12345678");
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe Updated", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(personProfileCommandService.handle(any(UpdatePersonProfileCommand.class), anyLong()))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        mockMvc.perform(put("/api/v1/person-profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated()) // Controller returns CREATED on update
                .andExpect(jsonPath("$.profileId").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe Updated"));
    }
}
