package org.tzi.use.api_security.rest.middleware;

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
import org.tzi.use.api_security.exceptions.ExceptionResponse;
import org.tzi.use.api_security.exceptions.Exceptions;
import org.tzi.use.api_security.rest.service.ApiTokenService;
import org.tzi.use.api_security.security.UserAuthentication;
import org.tzi.use.api_security.token.AccessToken;
import org.tzi.use.api_security.token.ApiToken;

@Component
public class AuthenticationMiddleware extends OncePerRequestFilter {
    @Autowired
    private ApiTokenService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("AuthenticationMiddleware start");

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Extract the token by removing the "Bearer " prefix
                String jwtString = authorizationHeader.substring(7);
                Optional<AccessToken> accessTokenOptional = ApiToken.fromTokenString(jwtString);
                AccessToken accessToken;
                Optional<ApiToken> apiToken;

                if (!accessTokenOptional.isPresent()) {
                    throw new Exceptions.InvalidException("Authorization Header does not contain a Valid AccessToken");
                }

                accessToken = accessTokenOptional.get();
                apiToken = ApiToken.fromAccessToken(accessToken);

                // If the accessToken is an apiToken (accessToken without expireTime)
                if (apiToken.isPresent()) {
                    // Since ApiTokens don't have an expire time, we check presents in db to make
                    // them manually revokable by removing
                    if (this.service.get(accessToken.getUid(), accessToken.getUserId()).isEmpty()) {
                        throw new Exceptions.ExpiredException("Token has expire");
                    }
                }

                UserAuthentication user = new UserAuthentication(accessToken.getUserId(), accessToken.getEmail(),
                        accessToken.getRoles());

                SecurityContextHolder.getContext().setAuthentication(user);
            } catch (Exceptions.InvalidException | Exceptions.ExpiredException e) {
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