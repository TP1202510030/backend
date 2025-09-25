package com.tp1202510030.backend.iam.application.internal.commandservices;

import com.tp1202510030.backend.iam.application.internal.outboundservices.acl.IamExternalCompanyService;
import com.tp1202510030.backend.iam.application.internal.outboundservices.hashing.HashingService;
import com.tp1202510030.backend.iam.application.internal.outboundservices.tokens.TokenService;
import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.commands.CreateUserCommand;
import com.tp1202510030.backend.iam.domain.model.commands.DeleteUserCommand;
import com.tp1202510030.backend.iam.domain.model.commands.DeleteUsersByCompanyId;
import com.tp1202510030.backend.iam.domain.model.commands.SignInCommand;
import com.tp1202510030.backend.iam.domain.services.user.UserCommandService;
import com.tp1202510030.backend.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.tp1202510030.backend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.tp1202510030.backend.shared.domain.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final IamExternalCompanyService externalCompanyService;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            HashingService hashingService,
            TokenService tokenService,
            IamExternalCompanyService iamExternalCompanyService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.externalCompanyService = iamExternalCompanyService;
    }

    @Override
    @Transactional
    public Optional<User> handle(CreateUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new RuntimeException("Username already exists");
        }

        var company = externalCompanyService.getCompanyById(command.companyId())
                .orElseThrow(() -> new RuntimeException("Company with ID " + command.companyId() + " not found"));

        var roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role name not found")))
                .toList();

        var user = new User(command.username(), hashingService.encode(command.password()), roles, company);
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", command.username()));
        if (!hashingService.matches(command.password(), user.getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.getUsername());
        return Optional.of(new ImmutablePair<>(user, token));
    }

    @Override
    public void handle(DeleteUserCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", command.userId().toString()));

        userRepository.delete(user);
    }

    @Override
    public void handle(DeleteUsersByCompanyId command) {
        var company = externalCompanyService.getCompanyById(command.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "ID", command.companyId().toString()));
        userRepository.deleteAllByCompanyId(company.getId());
    }
}
