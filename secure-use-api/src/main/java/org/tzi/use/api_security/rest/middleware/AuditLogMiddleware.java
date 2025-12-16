package org.tzi.use.api_security.rest.middleware;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tzi.use.api_security.security.UserAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuditLogMiddleware extends OncePerRequestFilter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    private final Logger logger = LoggerFactory.getLogger(AuditLogMiddleware.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("AuditLogMiddleware start");

        // Needing cachedResponse for getting Body which is a one time readable stream.
        // With not doing it that way but reading the body here, result in an empty body
        // for the client.
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);

        String requestTime = OffsetDateTime.now().format(dateTimeFormatter);
        String host = request.getRemoteAddr();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String protocol = request.getProtocol();
        String referer = request.getHeader("Referer");
        String userAgent = request.getHeader("User-Agent");

        // Continue the filter chain
        filterChain.doFilter(request, cachedResponse);

        Authentication anonymousUser = SecurityContextHolder.getContext().getAuthentication();
        UserAuthentication user = null;

        // If user is know and of instance UserAuthentication and not
        // org.springframework.security.authentication.AnonymousAuthenticationToken
        if (anonymousUser != null && anonymousUser instanceof UserAuthentication) {
            user = (UserAuthentication) anonymousUser;
        }

        // Log the response details
        int status = cachedResponse.getStatus();
        byte[] responseBody = cachedResponse.getContentAsByteArray();

        String logEntry = String.format("%s - %s [%s] \"%s %s %s\" %d %d %s %s",
                host,
                user != null ? user.getUserId() : "-",
                requestTime,
                method,
                requestURI,
                protocol,
                status,
                responseBody.length,
                referer != null ? "\"" + referer + "\"" : "-",
                userAgent != null ? "\"" + userAgent + "\"" : "-");

        logger.info(logEntry);

        cachedResponse.copyBodyToResponse();

        System.out.println("AuditLogMiddleware end");
    }
}