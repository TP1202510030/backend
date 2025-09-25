package com.tp1202510030.backend.iam.domain.services.user;

import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.commands.CreateUserCommand;
import com.tp1202510030.backend.iam.domain.model.commands.DeleteUserCommand;
import com.tp1202510030.backend.iam.domain.model.commands.DeleteUsersByCompanyId;
import com.tp1202510030.backend.iam.domain.model.commands.SignInCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(CreateUserCommand command);

    Optional<ImmutablePair<User, String>> handle(SignInCommand command);

    void handle(DeleteUserCommand command);

    void handle(DeleteUsersByCompanyId command);
}
