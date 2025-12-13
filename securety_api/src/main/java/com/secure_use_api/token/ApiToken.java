package com.secure_use_api.token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.secure_use_api.security.Role;

public class ApiToken extends AccessToken {
    public static Optional<ApiToken> fromAccessToken(AccessToken accessToken) {
        if (accessToken.isLongLife()) {
            return Optional.of(new ApiToken(accessToken.getUid(), accessToken.getUserId(), accessToken.getEmail(),
                    accessToken.getRoles()));
        }

        return Optional.empty();
    }

    public static Optional<ApiToken> fromString(String tokenString) {
        Optional<AccessToken> accessTokenOptional = AccessToken.fromTokenString(tokenString);

        if (accessTokenOptional.isPresent()) {
            return ApiToken.fromAccessToken(accessTokenOptional.get());
        }

        return Optional.empty();
    }

    public ApiToken(UUID userId, String email, List<Role> roles) {
        super(UUID.randomUUID(), userId, email, roles, Optional.empty());
    }

    private ApiToken(UUID uid, UUID userId, String email, List<Role> roles) {
        super(uid, userId, email, roles, Optional.empty());
    }

}
