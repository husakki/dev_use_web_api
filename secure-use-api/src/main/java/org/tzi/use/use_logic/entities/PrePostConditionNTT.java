package org.tzi.use.use_logic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrePostConditionNTT {
    private String operationName;
    private String name;
    private String condition;
    private boolean isPre;
}
