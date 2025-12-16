package org.tzi.use.use_logic.DTO;

public enum AggregationTypeDTO {
    NONE("association"),
    AGGREGATION("aggregation"),
    COMPOSITION("composition");

    private final String displayName;

    AggregationTypeDTO(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
