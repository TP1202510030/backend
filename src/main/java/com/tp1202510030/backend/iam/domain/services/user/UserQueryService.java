package com.tp1202510030.backend.iam.domain.services.user;

import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.queries.GetAllUsersByCompanyIdQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetAllUsersQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByIdQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByUsernameQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);

    Optional<User> handle(GetUserByIdQuery query);

    Optional<User> handle(GetUserByUsernameQuery query);

    Optional<Iterable<User>> handle(GetAllUsersByCompanyIdQuery query);
}
