package com.safecar.platform.devices.application.internal.queryservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safecar.platform.devices.domain.model.aggregates.Driver;
import com.safecar.platform.devices.domain.model.queries.GetDriverByProfileIdQuery;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.DriverRepository;

@ExtendWith(MockitoExtension.class)
public class DriverQueryServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverQueryServiceImpl driverQueryService;

    @Test
    public void handleGetDriverByProfileIdQuery_WhenDriverExists_ReturnsDriver() {
        Driver driver = new Driver(1L);
        driver.setId(1L);

        when(driverRepository.findByProfileId_ProfileId(1L)).thenReturn(Optional.of(driver));

        Optional<Driver> result = driverQueryService.handle(new GetDriverByProfileIdQuery(1L));

        assertThat(result).isPresent();
        assertThat(result.get().getProfileId()).isEqualTo(1L);
    }
}
