package com.secure_use_api.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.secure_use_api.model.UserComposite;

public class UserDetailsImpl implements UserDetails {

    private final UUID userId; // Custom field for user ID

    private final String email; // Email

    @JsonIgnore
    private String password;

    private final Collection<Role> authorities; // User roles

    public UserDetailsImpl(UUID userId, String email, String password,
            Collection<Role> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserComposite user) {

        List<Role> roles = user.getUserMeta().getRoles().stream()
                .map(Role::fromString)
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUserMeta().getUserId(),
                user.getUserMeta().getEmail(),
                user.getUserSecurity().getPassword(),
                roles);
    }

    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return this.authorities;
    }
}
