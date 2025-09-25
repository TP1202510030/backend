package com.tp1202510030.backend.iam.interfaces.rest.transform;

import com.tp1202510030.backend.iam.domain.model.commands.PatchUserCommand;
import com.tp1202510030.backend.iam.interfaces.rest.resources.PatchUserResource;

public class PatchUserCommandFromResourceAssembler {
    public static PatchUserCommand toCommandFromResource(Long userId, PatchUserResource resource) {
        return new PatchUserCommand(
                userId,
                resource.username(),
                resource.roles()
        );
    }
}
