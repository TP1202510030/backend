package com.tp1202510030.backend.iam.interfaces.acl;

import java.util.List;
import java.util.Optional;

public interface IamContextFacade {
    /**
     * Creates a user with a specific set of roles for a given company.
     *
     * @param username  The username for the new user.
     * @param password  The password for the new user.
     * @param companyId The ID of the company this user will belong to.
     * @param roleNames A list of role names (e.g., "ROLE_COMPANY_ADMIN"). If null or empty, a default role will be assigned.
     * @return The ID of the newly created user, if successful.
     */
    Optional<Long> createUser(String username, String password, Long companyId, List<String> roleNames);

    /**
     * Fetches a user's ID based on their username.
     *
     * @param username The username to search for.
     * @return The user's ID, if found.
     */
    Optional<Long> fetchUserIdByUsername(String username);

    /**
     * Fetches a username based on their user ID.
     *
     * @param userId The user's ID to search for.
     * @return The username, if found.
     */
    Optional<String> fetchUsernameByUserId(Long userId);
}