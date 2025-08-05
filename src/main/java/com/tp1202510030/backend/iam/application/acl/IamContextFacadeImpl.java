package com.tp1202510030.backend.iam.application.acl;

import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.commands.SignUpCommand;
import com.tp1202510030.backend.iam.domain.model.entities.Role;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByIdQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByUsernameQuery;
import com.tp1202510030.backend.iam.domain.services.UserCommandService;
import com.tp1202510030.backend.iam.domain.services.UserQueryService;
import com.tp1202510030.backend.iam.interfaces.acl.IamContextFacade;
import com.tp1202510030.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @Override
    public Optional<Long> createUser(String username, String password) {
        var signUpCommand = new SignUpCommand(username, password, List.of(Role.getDefaultRole()));
        return userCommandService.handle(signUpCommand).map(AuditableAbstractAggregateRoot::getId);
    }

    @Override
    public Optional<Long> createUser(String username, String password, List<String> roleNames) {
        var roles = roleNames == null ? new ArrayList<Role>() : roleNames.stream().map(Role::toRoleFromName).toList();
        var signUpCommand = new SignUpCommand(username, password, roles);
        return userCommandService.handle(signUpCommand).map(AuditableAbstractAggregateRoot::getId);
    }

    @Override
    public Optional<Long> fetchUserIdByUsername(String username) {
        var getUserByUsernameQuery = new GetUserByUsernameQuery(username);
        return userQueryService.handle(getUserByUsernameQuery).map(AuditableAbstractAggregateRoot::getId);
    }

    @Override
    public Optional<String> fetchUsernameByUserId(Long userId) {
        var getUserByUserIdQuery = new GetUserByIdQuery(userId);
        return userQueryService.handle(getUserByUserIdQuery).map(User::getUsername);
    }
}