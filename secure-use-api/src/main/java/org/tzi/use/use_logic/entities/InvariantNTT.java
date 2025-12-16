package org.tzi.use.use_logic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvariantNTT {
    private String invName;
    private String invBody;
    private boolean isExistential;
}
