package com.tp1202510030.backend.iam.interfaces.rest;

import com.tp1202510030.backend.iam.domain.model.queries.GetAllUsersQuery;
import com.tp1202510030.backend.iam.domain.model.queries.GetUserByIdQuery;
import com.tp1202510030.backend.iam.domain.services.UserCommandService;
import com.tp1202510030.backend.iam.domain.services.UserQueryService;
import com.tp1202510030.backend.iam.interfaces.rest.resources.CreateUserResource;
import com.tp1202510030.backend.iam.interfaces.rest.resources.UserResource;
import com.tp1202510030.backend.iam.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import com.tp1202510030.backend.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.tp1202510030.backend.shared.infrastructure.authorization.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UsersController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Get all the users available in the system.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        var userResources = users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(userResources);
    }

    /**
     * Get user by id.
     *
     * @param userId The id of the user to retrieve.
     * @return The user.
     */
    @GetMapping(value = "/{userId}")
    @Operation(
            summary = "Get user by id",
            description = "Get the user with the given id.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    @PreAuthorize(SecurityConstants.ADMIN_OR_USER_SELF)
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Create a new user for a company.
     *
     * @param resource The user to create.
     * @return The created user.
     */
    @PostMapping
    @Operation(summary = "Create a new user for a company",
            description = "Creates a new user associated with a specific company. This endpoint is for admin use only.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request (e.g., company not found, username exists)."),
            @ApiResponse(responseCode = "403", description = "Forbidden.")
    })
    @PreAuthorize(SecurityConstants.IS_ADMIN)
    public ResponseEntity<UserResource> createUser(@RequestBody CreateUserResource resource) {
        var createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(createUserCommand);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }
}
