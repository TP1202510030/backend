package com.tp1202510030.backend.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String username, Long companyId, String token) {
}
