package com.secure_use_api.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RefreshTokenDto {

    @NotNull(message = "RefreshToken cannot be null")
    @NotEmpty(message = "RefreshToken cannot be empty")
    @Size(min = 8, max = 4096)
    private String refreshToken;

    public RefreshTokenDto() {
        this.refreshToken = "";
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
}
