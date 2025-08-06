package com.tp1202510030.backend.iam.interfaces.rest;

import com.tp1202510030.backend.iam.domain.services.UserCommandService;
import com.tp1202510030.backend.iam.interfaces.rest.resources.CreateUserResource;
import com.tp1202510030.backend.iam.interfaces.rest.resources.UserResource;
import com.tp1202510030.backend.iam.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import com.tp1202510030.backend.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin", description = "User Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserCommandService userCommandService;

    public AdminController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/users")
    @Operation(summary = "Create a new user for a company", description = "Creates a new user associated with a specific company. This endpoint is for admin use only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request (e.g., company not found, username exists)."),
            @ApiResponse(responseCode = "403", description = "Forbidden.")
    })
    @PreAuthorize("hasRole('ADMIN')")
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