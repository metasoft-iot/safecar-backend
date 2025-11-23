package com.safecar.platform.workshop.infrastructure.persistence.jpa.seeding;

import com.safecar.platform.workshop.domain.model.entities.ServiceType;
import com.safecar.platform.workshop.domain.model.valueobjects.ServiceTypes;
import com.safecar.platform.workshop.infrastructure.persistence.jpa.ServiceTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * ServiceTypeDataSeeder
 * <p>
 *     This class is responsible for seeding the database with predefined service types.
 *     It follows the same pattern as RoleDataSeeder in IAM bounded context.
 * </p>
 */
@Configuration
public class ServiceTypeDataSeeder {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceTypeDataSeeder.class);
    
    /**
     * Initializes the database with predefined service types.
     * 
     * @param serviceTypeRepository the service type repository
     * @return CommandLineRunner that seeds service types
     */
    @Bean
    public CommandLineRunner seedServiceTypes(ServiceTypeRepository serviceTypeRepository) {
        return args -> {
            logger.info("Starting ServiceType data seeding...");
            
            Arrays.stream(ServiceTypes.values()).forEach(serviceTypeName -> {
                if (!serviceTypeRepository.existsByName(serviceTypeName)) {
                    var serviceType = new ServiceType(serviceTypeName);
                    serviceTypeRepository.save(serviceType);
                    logger.info("Seeded service type: {}", serviceTypeName);
                } else {
                    logger.debug("Service type {} already exists, skipping.", serviceTypeName);
                }
            });
            
            logger.info("ServiceType data seeding completed.");
        };
    }
}
