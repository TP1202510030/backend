package com.tp1202510030.backend.iam.domain.services;

import com.tp1202510030.backend.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
