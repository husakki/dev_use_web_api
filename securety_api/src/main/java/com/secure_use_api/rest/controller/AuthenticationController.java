package com.secure_use_api.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.secure_use_api.exceptions.Exceptions.ExpiredException;
import com.secure_use_api.exceptions.Exceptions.InvalidException;
import com.secure_use_api.exceptions.Exceptions.UserAlreadyExists;
import com.secure_use_api.rest.dto.TokensDto;
import com.secure_use_api.rest.dto.IdActionDto;
import com.secure_use_api.rest.dto.RefreshTokenDto;
import com.secure_use_api.rest.dto.UserLoginDto;
import com.secure_use_api.rest.dto.UserRegistrationDto;
import com.secure_use_api.rest.service.RefreshTokenService;
import com.secure_use_api.rest.service.UserService;
import com.secure_use_api.rest.service.AuthenticationService;
import com.secure_use_api.security.RequireScope;
import com.secure_use_api.security.Role;
import com.secure_use_api.security.UserAuthentication;
import com.secure_use_api.token.AccessToken;
import com.secure_use_api.token.RefreshToken;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping({ "", "/" })
    @RequireScope({ "authenticated", "account:read" })
    String home() {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        List<Role> roles = user.getAuthorities().stream()
                .filter(authority -> authority instanceof Role) // Filter to ensure it's a Role
                .map(authority -> (Role) authority) // Cast to Role
                .collect(Collectors.toList()); // Collect to List<Role>

        return "Hello World! with " + roles.toString();
    }

    @Transactional
    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdActionDto> register(@RequestBody @Valid UserRegistrationDto registrationDto) {

        try {
            UUID userId = this.authService.register(registrationDto.getEmail(), registrationDto.getPassword());

            this.userService.createUserAdditions(userId);

            return ResponseEntity.ok(new IdActionDto(userId, "UserCreated"));
        } catch (UserAlreadyExists err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getMessage());
        }
    }

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokensDto> login(@RequestBody @Valid UserLoginDto loginRequest) {
        try {
            AccessToken accessToken = this.authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            RefreshToken refreshToken = refreshTokenService.create(accessToken);

            System.out.println(accessToken);
            System.out.println("lbub");

            return ResponseEntity.ok(new TokensDto(accessToken.toTokenString(), refreshToken.toTokenString()));
        } catch (AuthenticationException err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getMessage());
        }
    }

    @PutMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokensDto> refresh(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid RefreshTokenDto refreshTokenRequest) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Authorization Bearer Token");
            }

            Optional<AccessToken> accessToken = AccessToken.fromTokenString(authorizationHeader.substring(7));
            Optional<RefreshToken> refreshToken = RefreshToken.fromTokenString(refreshTokenRequest.getRefreshToken());

            if (!accessToken.isPresent()) {
                throw new InvalidException("Authorization Header does not contain a Valid AccessToken");
            } else if (!refreshToken.isPresent()) {
                throw new InvalidException("RefreshToken is not a Valid Token");
            }

            AccessToken newAccessToken = refreshTokenService.devaluate(refreshToken.get(), accessToken.get());
            RefreshToken newRefreshToken = refreshTokenService.create(newAccessToken);

            return ResponseEntity.ok(new TokensDto(newAccessToken.toTokenString(), newRefreshToken.toTokenString()));
        } catch (JWTVerificationException | ExpiredException | InvalidException | IllegalArgumentException
                | AuthenticationException err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getMessage());
        }
    }

    @DeleteMapping(path = "delete")
    @RequireScope({ "authenticated", "account:delete" })
    public ResponseEntity<IdActionDto> delete() {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();

        this.authService.delete(user.getUserId());

        return ResponseEntity.ok(new IdActionDto(user.getUserId(), "UserDeleted"));
    }
}