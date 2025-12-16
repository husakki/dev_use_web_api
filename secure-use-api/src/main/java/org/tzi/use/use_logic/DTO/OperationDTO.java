package org.tzi.use.use_logic.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {

    private String operationName;
    private String[][] parameter;
    private String returnType;

}
