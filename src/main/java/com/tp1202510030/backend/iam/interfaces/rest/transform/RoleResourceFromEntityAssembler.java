package com.tp1202510030.backend.iam.interfaces.rest.transform;

import com.tp1202510030.backend.iam.domain.model.entities.Role;
import com.tp1202510030.backend.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(
                entity.getId(),
                entity.getStringName());
    }
}
