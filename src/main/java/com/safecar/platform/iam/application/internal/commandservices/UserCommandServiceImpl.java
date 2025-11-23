package com.safecar.platform.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import com.safecar.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.safecar.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.safecar.platform.iam.domain.model.aggregates.User;
import com.safecar.platform.iam.domain.model.commands.SignInCommand;
import com.safecar.platform.iam.domain.model.commands.SignUpCommand;
import com.safecar.platform.iam.domain.model.valueobjects.Email;
import com.safecar.platform.iam.domain.model.valueobjects.Roles;
import com.safecar.platform.iam.domain.services.UserCommandService;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.safecar.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserCommandServiceImpl
 * <p>
 * Implementation of UserCommandService.
 * This class is responsible for handling the SignUpCommand and SignInCommand
 * and persisting the user in the database.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    /**
     * Constructor
     * 
     * @param userRepository {@link UserRepository} instance
     * @param hashingService {@link HashingService} instance
     * @param tokenService   {@link TokenService} instance
     * @param roleRepository {@link RoleRepository} instance
     */
    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService,
            TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    // inherited javadoc
    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(new Email(command.email())))
            throw new RuntimeException("Email already exists");

        var roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());

        // If no roles were provided, assign the persisted default role
        if (roles.isEmpty()) {
            var defaultRole = roleRepository.findByName(Roles.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            roles.add(defaultRole);
        }

        var saved = userRepository.save(new User(
                command.email(),
                hashingService.encode(command.password()),
                roles));

        return Optional.of(saved);
    }

    // inherited javadoc
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(new Email(command.email()));

        if (user.isEmpty())
            throw new RuntimeException("User not found");

        var existingUser = user.get();
        
        if (!hashingService.matches(command.password(), existingUser.getPassword()))
            throw new RuntimeException("Invalid password");

        var token = tokenService.generateToken(existingUser.getEmail());

        return Optional.of(ImmutablePair.of(existingUser, token));
    }
}