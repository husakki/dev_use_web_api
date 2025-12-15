package org.tzi.use.use_logic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationNTT {

    private String operationName;
    private String[][] parameter;
    private String returnType;

}

