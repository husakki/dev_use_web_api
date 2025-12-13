package com.secure_use_api.rest.middleware;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.secure_use_api.exceptions.ExceptionResponse;
import com.secure_use_api.exceptions.Exceptions.ExpiredException;
import com.secure_use_api.exceptions.Exceptions.InvalidException;
import com.secure_use_api.rest.service.ApiTokenService;
import com.secure_use_api.security.UserAuthentication;
import com.secure_use_api.token.AccessToken;
import com.secure_use_api.token.ApiToken;

@Component
public class AuthenticationMiddleware extends OncePerRequestFilter {
    @Autowired
    private ApiTokenService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("AuthenticationMiddleware start");

        String authorizationHeader = request.getHeader("Authorization");

        System.out.println("hey");
        System.out.println(authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Extract the token by removing the "Bearer " prefix
                String jwtString = authorizationHeader.substring(7);
                Optional<AccessToken> accessTokenOptional = ApiToken.fromTokenString(jwtString);
                AccessToken accessToken;
                Optional<ApiToken> apiToken;

                if (!accessTokenOptional.isPresent()) {
                    throw new InvalidException("Authorization Header does not contain a Valid AccessToken");
                }

                accessToken = accessTokenOptional.get();
                apiToken = ApiToken.fromAccessToken(accessToken);

                // If the accessToken is an apiToken (accessToken without expireTime)
                if (apiToken.isPresent()) {
                    // Since ApiTokens don't have an expire time, we check presents in db to make
                    // them manually revokable by removing
                    if (this.service.get(accessToken.getUid(), accessToken.getUserId()).isEmpty()) {
                        throw new ExpiredException("Token has expire");
                    }
                }

                UserAuthentication user = new UserAuthentication(accessToken.getUserId(), accessToken.getEmail(),
                        accessToken.getRoles());

                SecurityContextHolder.getContext().setAuthentication(user);
            } catch (InvalidException | ExpiredException e) {
                ExceptionResponse exRes = new ExceptionResponse(HttpStatus.UNAUTHORIZED, "Authentication Token wrong",
                        request.getRequestURI());

                exRes.writeToResponse(response, request);

                return;
            }
        }

        filterChain.doFilter(request, response);

        System.out.println("AuthenticationMiddleware end");
    }

}