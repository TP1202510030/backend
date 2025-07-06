package com.tp1202510030.backend.iam.domain.model.entities;


import com.tp1202510030.backend.iam.domain.model.valueobjects.Roles;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
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
        return new Role(Roles.ROLE_USER);
    }

    public static List<Role> validateRoleSet(List<Role> roles) {
        if (Objects.isNull(roles) || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }
}
