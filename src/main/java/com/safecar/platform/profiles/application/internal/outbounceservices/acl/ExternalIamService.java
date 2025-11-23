package com.safecar.platform.profiles.application.internal.outbounceservices.acl;

import java.util.Set;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.safecar.platform.iam.interfaces.acl.IamContextFacade;

@Service("externalIamService")
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;
    private final Logger logger = Logger.getLogger(ExternalIamService.class.getName());

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Set<String> fetchUserRolesByUserEmail(String userEmail) {
        logger.info("Fetching roles for user email: " + userEmail);
        var userId = iamContextFacade.fetchUserIdByEmail(userEmail);
        logger.info("Found user ID: " + userId + " for email: " + userEmail);
        var roles = iamContextFacade.fetchUserRolesByUserId(userId);
        logger.info("Found roles: " + roles + " for user ID: " + userId);
        return roles;
    }

    public boolean validateUserExistsByEmail(String userEmail) {
        return iamContextFacade.validateUserExistsByEmail(userEmail);
    }
}
