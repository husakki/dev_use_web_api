package org.tzi.use.api_security.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TokensDto {
    @NotNull
    @Size(min = 8, max = 4096)
    private final String token;

    @Size(min = 8, max = 4096)
    private final String refreshToken;

    public TokensDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return this.token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
}
