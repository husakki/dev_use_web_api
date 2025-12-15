package org.tzi.use.api_security.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserLoginDto {

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 4, max = 56)
    @Email
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 56)
    private String password;

    public UserLoginDto() {
        this.email = "";
        this.password = "";
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
