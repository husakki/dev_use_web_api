package com.secure_use_api.rest.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IdActionDto {

    @Size(min = 1, max = 32)
    private String actionType;

    @NotNull
    private UUID id;

    public IdActionDto(UUID id, String actionType) {
        this.id = id;
        this.actionType = actionType;
    }

    public UUID getId() {
        return this.id;
    }

    public String getActionType() {
        return this.actionType;
    }
}
