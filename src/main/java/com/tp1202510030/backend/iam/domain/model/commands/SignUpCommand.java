package com.tp1202510030.backend.iam.domain.model.commands;

import com.tp1202510030.backend.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(String username, String password, List<Role> roles) {
}
