package com.secure_use_api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.secure_use_api.rest.dto.ResourceLimitLeftDto;
import com.secure_use_api.rest.middleware.ResourceLimitMiddleware.ResourceLimited;
import com.secure_use_api.rest.service.UserService;
import com.secure_use_api.security.RequireScope;
import com.secure_use_api.security.UserAuthentication;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "longRun")
    @ResourceLimited
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