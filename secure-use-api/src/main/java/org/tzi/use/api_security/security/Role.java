package org.tzi.use.api_security.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    public static enum Roles {
        API_TOKEN,
        USER,
    }

    private static final Map<Roles, List<String>> roleScopesMap = new HashMap<>();

    static {
        // Initialize the map with roles and their corresponding scopes
        roleScopesMap.put(Roles.USER, List.of("authenticated", "account:read", "account:delete", "token:create",
                "token:read", "token:delete"));
        roleScopesMap.put(Roles.API_TOKEN, List.of("authenticated", "account:read", "token:read"));
    }

    private final Roles role;

    private Collection<String> scopes;

    public static boolean checkScope(Collection<Role> roles, String[] requiredScopes) {
        List<String> requiredScopesList = new ArrayList<>(Arrays.asList(requiredScopes));

        for (Role role : roles) {
            if (requiredScopesList.isEmpty()) {
                break;
            }

            requiredScopesList.removeAll(role.getScopes());
        }

        // If all requiredScopes where removed by being present in roles, the check is
        // successful
        return requiredScopesList.isEmpty();
    }

    public Role(Roles role) {
        this.role = role;

        Collection<String> scopes = roleScopesMap.get(role);

        this.scopes = scopes != null ? scopes : List.of();
    }

    @Override
    public String getAuthority() {
        return this.role.toString();
    }

    public Collection<String> getScopes() {
        return this.scopes;
    }

    @Override
    public String toString() {
        return this.role.toString();
    }

    public static Role fromString(String roleString) {
        return new Role(Roles.valueOf(roleString));
    }
}
