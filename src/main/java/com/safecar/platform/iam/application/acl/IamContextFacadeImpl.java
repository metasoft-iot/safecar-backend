package com.safecar.platform.iam.application.acl;

import com.safecar.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.safecar.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.safecar.platform.iam.domain.services.UserQueryService;
import com.safecar.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * IamContextFacadeImpl
 * <p>
 * This class provides the implementation of the IamContextFacade interface.
 * This class is used by other bounded contexts to interact with the IAM module
 * for user validation and information retrieval.
 * </p>
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {

    private final UserQueryService userQueryService;

    /**
     * Constructor
     * 
     * @param userQueryService the {@link UserQueryService} user query service
     */
    public IamContextFacadeImpl(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    // inherited javadoc
    @Override
    public Long fetchUserIdByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var result = userQueryService.handle(getUserByEmailQuery);
        if (result.isEmpty())
            return 0L;
        return result.get().getId();
    }

    // inherited javadoc
    @Override
    public String fetchUserEmailByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        if (result.isEmpty())
            return "";
        return result.get().getEmail();
    }

    // inherited javadoc
    @Override
    public boolean validateUserExists(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);
        return result.isPresent();
    }

    // inherited javadoc
    @Override
    public boolean validateUserExistsByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var result = userQueryService.handle(getUserByEmailQuery);
        return result.isPresent();
    }

    // inherited javadoc
    @Override
    public Set<String> fetchUserRolesByUserId(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var result = userQueryService.handle(getUserByIdQuery);

        if (result.isEmpty())
            return Set.of();

        return result.get().getRolesAsStringSet();
    }
}
