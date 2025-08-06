package com.tp1202510030.backend.iam.interfaces.rest.resources;

import java.util.List;

public record CreateUserResource(String username, String password, Long companyId, List<String> roles) {
}
