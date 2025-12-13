package com.secure_use_api.rest.middleware;

import java.io.IOException;
import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.secure_use_api.exceptions.ExceptionResponse;
import com.secure_use_api.security.RequireScope;
import com.secure_use_api.security.Role;
import com.secure_use_api.security.UserAuthentication;

import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationMiddleware implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {

        System.out.println("AuthorizationMiddleware start");

        // Checking for handler to get the target controller method and associated
        // Annotations (Reason why this is an implementation from HandlerInterceptor and
        // not Filter)

        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequireScope requireScope = method.getAnnotation(RequireScope.class);

            if (requireScope != null) {
                String[] requiredScopes = requireScope.value();
                UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext()
                        .getAuthentication();

                // If no user is present, but the method has at leas an empty @RequireScope,
                // access is permitted
                if (user == null) {
                    ExceptionResponse exRes = new ExceptionResponse(HttpStatus.UNAUTHORIZED,
                            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                            request.getRequestURI());

                    exRes.writeToResponse(response, request);

                    return false;
                }

                // Check if the user has the required Scopes
                if (!Role.checkScope(user.getRoles(), requiredScopes)) {
                    ExceptionResponse exRes = new ExceptionResponse(HttpStatus.FORBIDDEN,
                            HttpStatus.FORBIDDEN.getReasonPhrase(),
                            request.getRequestURI());

                    exRes.writeToResponse(response, request);

                    return false;
                }
            }
        }

        System.out.println("AuthorizationMiddleware end");

        return true; // Continue the request
    }
}