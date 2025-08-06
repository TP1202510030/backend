package com.tp1202510030.backend.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tp1202510030.backend.iam.domain.model.aggregates.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String username;
    private final Long companyId;
    @JsonIgnore
    private final String password;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, Long companyId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.companyId = companyId;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public static UserDetailsImpl build(User user) {
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();

        Long companyId = Objects.nonNull(user.getCompany()) ? user.getCompany().getId() : null;

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                companyId,
                user.getPassword(),
                authorities
        );
    }
}