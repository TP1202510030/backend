package com.tp1202510030.backend.iam.domain.model.aggregates;

import com.tp1202510030.backend.companies.domain.model.aggregates.Company;
import com.tp1202510030.backend.iam.domain.model.entities.Role;
import com.tp1202510030.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "app_user")
public class User extends AuditableAbstractAggregateRoot<User> {
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        this.roles = new HashSet<>();
    }

    public User(String username, String password, Company company) {
        this();
        this.username = username;
        this.password = password;
        this.company = company;
    }

    public User(String username, String password, List<Role> roles, Company company) {
        this(username, password, company);
        addRoles(roles);
    }

    public void addRoles(List<Role> roles) {
        var validatedRoles = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoles);
    }
}