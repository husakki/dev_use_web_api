package com.secure_use_api.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResourceLimitLeftDto {

    @NotNull
    @Min(-999999999)
    @Max(999999999)
    private final long time;

    @Size(min = 1, max = 32)
    private final String unitType;

    public ResourceLimitLeftDto(long time, String unitType) {
        this.time = time;
        this.unitType = unitType;
    }

    public long getTime() {
        return this.time;
    }

    public String getUnitType() {
        return this.unitType;
    }
}
