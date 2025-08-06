package com.tp1202510030.backend.iam.interfaces.rest.transform;

import com.tp1202510030.backend.iam.domain.model.commands.CreateUserCommand;
import com.tp1202510030.backend.iam.interfaces.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateUserCommand toCommandFromResource(CreateUserResource resource) {
        var roles = RoleListFromStringAssembler.toRoleListFromStringList(resource.roles());
        return new CreateUserCommand(resource.username(), resource.password(), resource.companyId(), roles);
    }
}
