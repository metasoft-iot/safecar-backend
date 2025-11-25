package com.safecar.platform.profiles.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.profiles.domain.model.aggregates.PersonProfile;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetPersonProfileByUserEmailQuery;
import com.safecar.platform.profiles.domain.model.valueobjects.Dni;
import com.safecar.platform.profiles.domain.model.valueobjects.Phone;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.PersonProfileRepository;

@ExtendWith(MockitoExtension.class)
public class PersonProfileQueryServiceImplTest {

    @Mock
    private PersonProfileRepository personProfileRepository;

    @InjectMocks
    private PersonProfileQueryServiceImpl personProfileQueryService;

    @Test
    public void handleGetPersonProfileByUserEmailQuery_WhenProfileExists_ReturnsProfile() {
        // Arrange
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));

        when(personProfileRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(profile));

        // Act
        Optional<PersonProfile> result = personProfileQueryService
                .handle(new GetPersonProfileByUserEmailQuery("test@example.com"));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUserEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void handleGetPersonProfileByIdQuery_WhenProfileExists_ReturnsProfile() {
        // Arrange
        PersonProfile profile = new PersonProfile("test@example.com", "John Doe", "City", "Country",
                new Phone("123456789"), new Dni("12345678"));
        profile.setId(1L);

        when(personProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        // Act
        Optional<PersonProfile> result = personProfileQueryService.handle(new GetPersonProfileByIdQuery(1L));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }
}
