package org.tzi.use.api_security.rest.middleware;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tzi.use.api_security.exceptions.ExceptionResponse;

// Allows Filters to throw the custom ExceptionResponse which is written to the response and is made to be read by the client
@Component
public class ExceptionHandlerMiddleware extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExceptionResponse e) {
            e.writeToResponse(response, request);
        }
    }
}