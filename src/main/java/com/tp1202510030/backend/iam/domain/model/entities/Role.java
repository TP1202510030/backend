package com.tp1202510030.backend.iam.domain.model.entities;


import com.tp1202510030.backend.iam.domain.model.valueobjects.Roles;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Role entity in the system.
 * Roles are used to grant permissions to users.
 * This entity uses specific Lombok annotations instead of @Data to avoid potential issues with JPA proxies and lazy loading.
 */
@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true)
    private Roles name;

    public Role(Roles name) {
        this.name = name;
    }

    public String getStringName() {
        return name.name();
    }

    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_COMPANY_TECHNICIAN);
    }

    public static List<Role> validateRoleSet(List<Role> roles) {
        if (Objects.isNull(roles) || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }
}
