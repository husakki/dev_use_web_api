package org.tzi.use.api_security.token;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import org.tzi.use.api_security.security.Role;
import org.tzi.use.api_security.util.Config;

public class AccessToken extends AbstractToken {
    static long expirationTime = 720000;
    protected static SimpleTokenStringHandler tokenStringHandler = new JwtHandler(
            Config.getInstance().getSecretAccessTokenKey());

    protected final UUID uid;
    protected final UUID userId;
    protected final String email;
    protected final List<Role> roles;
    protected final Optional<Date> expiresAt;

    public class InnerAccessToken {
        public String jti;
        public String sub;
        public String userId;
        public Long exp;
        public List<String> roles;
    }

    public AccessToken(UUID userId, String email, List<Role> roles) {
        this.uid = UUID.randomUUID();
        this.userId = userId;
        this.email = email;
        this.roles = roles;
        this.expiresAt = Optional.of(new Date(System.currentTimeMillis() + expirationTime));
    }

    protected AccessToken(UUID uid, UUID userId, String email, List<Role> roles, Optional<Date> expiresAt) {
        this.uid = uid;
        this.userId = userId;
        this.email = email;
        this.roles = roles;
        this.expiresAt = expiresAt;
    }

    static public Optional<AccessToken> fromTokenString(String tokeString) throws IllegalArgumentException {
        Optional<String> payloadString = tokenStringHandler.payloadFromTokenString(tokeString);

        if (payloadString.isPresent()) {
            Gson json = new Gson();
            InnerAccessToken payload = json.fromJson(payloadString.get(), InnerAccessToken.class);

            String uidString = payload.jti;
            String email = payload.sub;
            Long expiresAtLong = payload.exp;
            String userIdString = payload.userId;
            List<String> rolesAStrings = payload.roles;
            List<Role> roles;

            if (uidString == null || uidString.isEmpty()) {
                throw new IllegalArgumentException("Missing uid");
            }

            if (userIdString == null || userIdString.isEmpty()) {
                throw new IllegalArgumentException("Missing userId");
            }

            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Missing email");
            }

            if (rolesAStrings == null) {
                throw new IllegalArgumentException("No roles present");
            } else {
                roles = rolesAStrings.stream()
                        .map(Role::fromString)
                        .collect(Collectors.toList());
            }

            UUID uid = UUID.fromString(uidString);
            UUID userId = UUID.fromString(userIdString);
            Optional<Date> expiresAtDate = expiresAtLong != null ? Optional.of(new Date(expiresAtLong * 1000))
                    : Optional.empty();

            return Optional.of(new AccessToken(uid, userId, email, roles, expiresAtDate));
        }

        return Optional.empty();
    }

    public String toTokenString() {
        Map<String, Object> payload = new HashMap<>();
        List<String> rolesAsStrings = this.roles.stream()
                .map(Role::toString)
                .collect(Collectors.toList());

        payload.put("jti", this.uid.toString());
        payload.put("sub", this.email);
        payload.put("iat", new Date().getTime() / 1000);
        payload.put("userId", this.userId.toString());
        payload.put("roles", rolesAsStrings);

        if (this.expiresAt.isPresent()) {
            payload.put("exp", this.expiresAt.get().getTime() / 1000);
        }

        return tokenStringHandler.payloadToTokenString(payload);
    }

    public UUID getUid() {
        return this.uid;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public boolean isLongLife() {
        return !this.expiresAt.isPresent();
    }
}
