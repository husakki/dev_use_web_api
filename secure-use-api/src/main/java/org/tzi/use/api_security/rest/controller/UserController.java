package org.tzi.use.api_security.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;

import org.tzi.use.api_security.rest.dto.ResourceLimitLeftDto;
import org.tzi.use.api_security.rest.middleware.ResourceLimitMiddleware;
import org.tzi.use.api_security.rest.service.UserService;
import org.tzi.use.api_security.security.RequireScope;
import org.tzi.use.api_security.security.UserAuthentication;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "longRun")
    @ResourceLimitMiddleware.ResourceLimited
    public ResponseEntity<String> longRunningTask() {
        try {
            // Simulate a long-running task (e.g., 20 seconds)
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle the interruption
        }

        return ResponseEntity.ok("Finished");
    }

    @Transactional
    @GetMapping(path = "resourceLimit")
    @RequireScope({ "authenticated", "account:read" })
    public ResponseEntity<ResourceLimitLeftDto> getResourceLimitLeft() {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();

        long resourceLimit = userService.getResourceLimitLeft(user.getUserId());

        return ResponseEntity.ok(new ResourceLimitLeftDto(resourceLimit, "seconds"));
    }
}