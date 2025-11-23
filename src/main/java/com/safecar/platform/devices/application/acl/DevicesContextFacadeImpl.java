package com.safecar.platform.devices.application.acl;

import com.safecar.platform.devices.domain.model.commands.CreateDriverCommand;
import com.safecar.platform.devices.domain.model.queries.GetDriverByProfileIdQuery;
import com.safecar.platform.devices.domain.model.queries.GetVehicleByIdQuery;
import com.safecar.platform.devices.domain.services.DriverCommandService;
import com.safecar.platform.devices.domain.services.DriverQueryService;
import com.safecar.platform.devices.domain.services.VehicleQueryService;
import com.safecar.platform.devices.infrastructure.persistence.jpa.repositories.VehicleRepository;
import com.safecar.platform.devices.interfaces.acl.DevicesContextFacade;
import com.safecar.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * DevicesContextFacadeImpl
 * <p>
 *     This class provides the implementation of the DevicesContextFacade interface.
 *     This class is used by other bounded contexts to interact with the Devices module
 *     for vehicle validation and information retrieval.
 * </p>
 */
@Service
public class DevicesContextFacadeImpl implements DevicesContextFacade {
    
    private static final Logger logger = LoggerFactory.getLogger(DevicesContextFacadeImpl.class);
    
    private final VehicleQueryService vehicleQueryService;
    private final VehicleRepository vehicleRepository;
    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;

    /**
     * Constructor
     *
     * @param vehicleQueryService Vehicle Query Service
     * @param vehicleRepository Vehicle Repository
     * @param driverCommandService Driver Command Service
     * @param driverQueryService Driver Query Service
     */
    public DevicesContextFacadeImpl(VehicleQueryService vehicleQueryService,
                                   VehicleRepository vehicleRepository,
                                   DriverCommandService driverCommandService,
                                   DriverQueryService driverQueryService) {
        this.vehicleQueryService = vehicleQueryService;
        this.vehicleRepository = vehicleRepository;
        this.driverCommandService = driverCommandService;
        this.driverQueryService = driverQueryService;
    }

    // inherited javadoc
    @Override
    public boolean validateVehicleExists(Long vehicleId) {
        try {
            var query = new GetVehicleByIdQuery(vehicleId);
            var vehicle = vehicleQueryService.handle(query);
            return vehicle.isPresent();
        } catch (Exception e) {
            logger.error("Error validating vehicle existence for ID {}: {}", vehicleId, e.getMessage());
            return false;
        }
    }

    // inherited javadoc
    @Override
    public boolean validateVehicleExistsByLicensePlate(String licensePlate) {
        try {
            return vehicleRepository.existsByLicensePlate(licensePlate);
        } catch (Exception e) {
            logger.error("Error validating vehicle existence for license plate {}: {}", licensePlate, e.getMessage());
            return false;
        }
    }

    // inherited javadoc
    @Override
    public Long fetchVehicleDriverId(Long vehicleId) {
        try {
            var query = new GetVehicleByIdQuery(vehicleId);
            var vehicle = vehicleQueryService.handle(query);
            return vehicle.map(v -> v.getDriverId()).orElse(0L);
        } catch (Exception e) {
            logger.error("Error fetching driver ID for vehicle {}: {}", vehicleId, e.getMessage());
            return 0L;
        }
    }

    // inherited javadoc
    @Override
    public String fetchVehicleLicensePlate(Long vehicleId) {
        try {
            var query = new GetVehicleByIdQuery(vehicleId);
            var vehicle = vehicleQueryService.handle(query);
            return vehicle.map(v -> v.getLicensePlate()).orElse("");
        } catch (Exception e) {
            logger.error("Error fetching license plate for vehicle {}: {}", vehicleId, e.getMessage());
            return "";
        }
    }

    // inherited javadoc
    @Override
    public String fetchVehicleDetails(Long vehicleId) {
        try {
            var query = new GetVehicleByIdQuery(vehicleId);
            var vehicle = vehicleQueryService.handle(query);
            return vehicle.map(v -> v.getBrand() + " " + v.getModel())
                         .orElse("Unknown Vehicle");
        } catch (Exception e) {
            logger.error("Error fetching vehicle details for ID {}: {}", vehicleId, e.getMessage());
            return "Unknown Vehicle";
        }
    }

    // inherited javadoc
    @Override
    public boolean validateDriverOwnsVehicle(Long vehicleId, Long driverId) {
        try {
            var query = new GetVehicleByIdQuery(vehicleId);
            var vehicle = vehicleQueryService.handle(query);
            return vehicle.map(v -> v.getDriverId().equals(driverId)).orElse(false);
        } catch (Exception e) {
            logger.error("Error validating driver ownership for vehicle {} and driver {}: {}", 
                        vehicleId, driverId, e.getMessage());
            return false;
        }
    }

    // inherited javadoc
    @Override
    public Long createDriver(Long profileId) {
        try {
            var command = new CreateDriverCommand(profileId);
            var driver = driverCommandService.handle(command);
            return driver.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
        } catch (Exception e) {
            logger.error("Error creating driver for profile {}: {}", profileId, e.getMessage());
            return 0L;
        }
    }

    // inherited javadoc
    @Override
    public boolean existsDriverByProfileId(Long profileId) {
        try {
            var query = new GetDriverByProfileIdQuery(profileId);
            var driver = driverQueryService.handle(query);
            return driver.isPresent();
        } catch (Exception e) {
            logger.error("Error checking driver existence for profile {}: {}", profileId, e.getMessage());
            return false;
        }
    }

    // inherited javadoc
    @Override
    public Long getDriverIdByProfileId(Long profileId) {
        try {
            var query = new GetDriverByProfileIdQuery(profileId);
            var driver = driverQueryService.handle(query);
            return driver.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
        } catch (Exception e) {
            logger.error("Error getting driver ID for profile {}: {}", profileId, e.getMessage());
            return 0L;
        }
    }
}