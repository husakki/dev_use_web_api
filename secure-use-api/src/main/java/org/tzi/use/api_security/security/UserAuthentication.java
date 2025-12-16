package org.tzi.use.api_security.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication implements Authentication {

    private final UUID userId; // Custom field for user ID
    private final String email; // Username
    private Object credentials;
    private Object details;
    private final Collection<Role> roles; // User roles
    private boolean authenticated; // Authentication status

    public UserAuthentication(UUID userId, String email, Collection<Role> authorities) {
        this.userId = userId;
        this.email = email;
        this.roles = authorities != null ? authorities : new ArrayList<>();
        this.authenticated = true; // Set to true upon successful authentication
    }

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
