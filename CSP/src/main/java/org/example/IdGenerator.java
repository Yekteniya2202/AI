package org.example;

import static org.example.VariableIdentity.id;

public class IdGenerator {
    public static VariableIdentity identityOfVariableAt(final int row, final int col) {
        return id(row, col);
    }
}
