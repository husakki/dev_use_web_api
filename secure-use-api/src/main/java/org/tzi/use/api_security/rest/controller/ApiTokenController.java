package org.tzi.use.api_security.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.context.SecurityContextHolder;

import org.tzi.use.api_security.exceptions.Exceptions;
import org.tzi.use.api_security.rest.dto.IdActionDto;
import org.tzi.use.api_security.rest.dto.TokensDto;
import org.tzi.use.api_security.rest.service.ApiTokenService;
import org.tzi.use.api_security.security.RequireScope;
import org.tzi.use.api_security.security.UserAuthentication;
import org.tzi.use.api_security.token.ApiToken;

@RestController
@RequestMapping("api")
public class ApiTokenController {

    @Autowired
    private ApiTokenService service;

    @GetMapping(path = "getAll")
    @RequireScope({ "authenticated", "token:read" })
    public ResponseEntity<List<String>> getAll() {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();

        try {
            List<String> apiTokenList = this.service.getAllAsStrings(user.getUserId());

            return ResponseEntity.ok(apiTokenList);
        } catch (Exceptions.UserAlreadyExists err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getMessage());
        }
    }

    @PostMapping(path = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireScope({ "authenticated", "token:create" })
    public ResponseEntity<TokensDto> create() {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();

        try {
            ApiToken apiToken = this.service.create(user.getUserId(), user.getName());

            return ResponseEntity.ok(new TokensDto(apiToken.toTokenString(), null));
        } catch (Exceptions.UserAlreadyExists err) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getMessage());
        }
    }

    @DeleteMapping(path = "delete/{apiTokenId}")
    @RequireScope({ "authenticated", "token:delete" })
    public ResponseEntity<IdActionDto> delete(@PathVariable UUID apiTokenId) {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();

        int deleteCount = this.service.delete(apiTokenId, user.getUserId());

        if (deleteCount != 0) {
            return ResponseEntity.ok(new IdActionDto(apiTokenId, "TokenDeleted"));
        }

        // This can be happen when there is not token with the given id or it is not
        // owned by the user
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Token deleted");
    }
}