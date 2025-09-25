package com.tp1202510030.backend.iam.interfaces.rest;

import com.tp1202510030.backend.iam.application.internal.outboundservices.cookies.CookieService;
import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.domain.model.valueobjects.ClientType;
import com.tp1202510030.backend.iam.domain.model.valueobjects.Roles;
import com.tp1202510030.backend.iam.domain.services.user.UserCommandService;
import com.tp1202510030.backend.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.tp1202510030.backend.iam.interfaces.rest.resources.SignInResource;
import com.tp1202510030.backend.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.tp1202510030.backend.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Available Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;
    private final CookieService cookieService;

    public AuthenticationController(UserCommandService userCommandService, CookieService cookieService) {
        this.userCommandService = userCommandService;
        this.cookieService = cookieService;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in a user", description = "Sign in a user with the provided username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource, HttpServletResponse response) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var authenticatedUserResult = userCommandService.handle(signInCommand);

        if (authenticatedUserResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var authenticatedUser = authenticatedUserResult.get();

        User userEntity = authenticatedUser.getLeft();
        boolean isAdmin = userEntity.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Roles.ROLE_ADMIN));


        String token = authenticatedUser.getRight();

        if (resource.clientType() == ClientType.WEB) {
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            response.addCookie(cookieService.createAuthCookie(token));
            var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(authenticatedUser.getLeft(), null);
            return ResponseEntity.ok(authenticatedUserResource);
        } else {
            var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(authenticatedUser.getLeft(), token);
            return ResponseEntity.ok(authenticatedUserResource);
        }
    }

    @PostMapping("/sign-out")
    @Operation(summary = "Sign out a user", description = "Signs out the current user by invalidating the authentication cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed out successfully.")
    })
    public ResponseEntity<Void> signOut(HttpServletResponse response) {
        response.addCookie(cookieService.createInvalidationCookie());
        return ResponseEntity.ok().build();
    }
}
