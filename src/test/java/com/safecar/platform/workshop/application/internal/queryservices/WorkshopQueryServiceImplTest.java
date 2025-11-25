package com.safecar.platform.workshop.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.shared.domain.model.valueobjects.ProfileId;
import com.safecar.platform.workshop.domain.model.aggregates.Workshop;
import com.safecar.platform.workshop.domain.model.queries.GetAllWorkshopsQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByBusinessProfileIdQuery;
import com.safecar.platform.workshop.domain.model.queries.GetWorkshopByIdQuery;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.repositories.WorkshopRepository;

@ExtendWith(MockitoExtension.class)
public class WorkshopQueryServiceImplTest {

    @Mock
    private WorkshopRepository workshopRepository;

    @InjectMocks
    private WorkshopQueryServiceImpl workshopQueryService;

    @Test
    public void handleGetWorkshopByIdQuery_WhenWorkshopExists_ReturnsWorkshop() {
        // Arrange
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopRepository.findById(1L)).thenReturn(Optional.of(workshop));

        // Act
        Optional<Workshop> result = workshopQueryService.handle(new GetWorkshopByIdQuery(1L));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getWorkshopDescription()).isEqualTo("Description");
    }

    @Test
    public void handleGetAllWorkshopsQuery_ReturnsWorkshops() {
        // Arrange
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopRepository.findAll()).thenReturn(List.of(workshop));

        // Act
        List<Workshop> result = workshopQueryService.handle(new GetAllWorkshopsQuery());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getWorkshopDescription()).isEqualTo("Description");
    }

    @Test
    public void handleGetWorkshopByBusinessProfileIdQuery_WhenWorkshopExists_ReturnsWorkshop() {
        // Arrange
        Workshop workshop = new Workshop(1L, "Description");
        workshop.setId(1L);

        when(workshopRepository.findByBusinessProfileId(any(ProfileId.class))).thenReturn(Optional.of(workshop));

        // Act
        Optional<Workshop> result = workshopQueryService.handle(new GetWorkshopByBusinessProfileIdQuery(1L));

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getBusinessProfileId()).isEqualTo(1L);
    }
}
