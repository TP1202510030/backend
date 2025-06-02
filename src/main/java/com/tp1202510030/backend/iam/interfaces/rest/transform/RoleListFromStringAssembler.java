package com.tp1202510030.backend.iam.interfaces.rest.transform;

import com.tp1202510030.backend.iam.domain.model.entities.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoleListFromStringAssembler {
    public static List<Role> toRoleListFromStringList(List<String> resourceList) {
        return Objects.nonNull(resourceList) ? resourceList.stream()
                .map(Role::toRoleFromName).toList() : new ArrayList<>();
    }
}
