package com.secure_use_api.rest.middleware;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.util.concurrent.RateLimiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.secure_use_api.exceptions.ExceptionResponse;
import com.secure_use_api.util.Config;

@Component
public class RateLimitMiddleware extends OncePerRequestFilter {

    // Create a rate limiter allowing 1000 requests per second
    private RateLimiter rateLimiter = RateLimiter.create(Config.getInstance().getRateLimitPerSecond());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("RateLimitMiddleware start");

        // Acquire a permit or return 429 Too Many Requests
        if (!rateLimiter.tryAcquire()) {
            ExceptionResponse exRes = new ExceptionResponse(HttpStatus.TOO_MANY_REQUESTS, new String("Try again later"),
                    request.getRequestURI());

            exRes.writeToResponse(response, request);

            return;
        }

        filterChain.doFilter(request, response);

        System.out.println("RateLimitMiddleware end");
    }

}