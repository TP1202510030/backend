package com.tp1202510030.backend.iam.interfaces.rest.transform;

import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import com.tp1202510030.backend.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
                entity.getId(),
                entity.getUsername(),
                RoleStringListFromEntityListAssembler.toResourceListFromEntitySet(entity.getRoles()));
    }
}
