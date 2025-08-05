package com.tp1202510030.backend.iam.interfaces.acl;

import java.util.List;
import java.util.Optional;

public interface IamContextFacade {
    Optional<Long> createUser(String username, String password);

    Optional<Long> createUser(String username, String password, List<String> roles);

    Optional<Long> fetchUserIdByUsername(String username);

    Optional<String> fetchUsernameByUserId(Long userId);
}