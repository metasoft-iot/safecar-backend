package com.safecar.platform.profiles.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.safecar.platform.profiles.domain.model.queries.GetBusinessProfileByUserEmailQuery;
import com.safecar.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessProfileRepository;

@ExtendWith(MockitoExtension.class)
public class BusinessProfileQueryServiceImplTest {

    @Mock
    private BusinessProfileRepository businessProfileRepository;

    @InjectMocks
    private BusinessProfileQueryServiceImpl businessProfileQueryService;

    @Test
    public void handleGetBusinessProfileByIdQuery_WhenProfileExists_ReturnsProfile() {
        // Arrange
        BusinessProfile profile = new BusinessProfile("test@example.com", "Business Name", "12345678901", "Address",
                "123456789", "contact@example.com", "Description");
        profile.setId(1L);

        when(businessProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        // Act
        Optional<BusinessProfile> result = businessProfileQueryService.handle(new GetBusinessProfileByIdQuery(1L));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    public void handleGetBusinessProfileByUserEmailQuery_WhenProfileExists_ReturnsProfile() {
        // Arrange
        BusinessProfile profile = new BusinessProfile("test@example.com", "Business Name", "12345678901", "Address",
                "123456789", "contact@example.com", "Description");

        when(businessProfileRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(profile));

        // Act
        Optional<BusinessProfile> result = businessProfileQueryService
                .handle(new GetBusinessProfileByUserEmailQuery("test@example.com"));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUserEmail()).isEqualTo("test@example.com");
    }
}
