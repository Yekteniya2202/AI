package org.example;

/**
 * Typed identity for {@code Variable}s which uniquely identifies a {@code Variable} of a CSP.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class VariableIdentity {

    private final int row, col;

    public VariableIdentity(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableIdentity that = (VariableIdentity) o;

        return that.row == row && that.col == col;
    }

    @Override
    public int hashCode() {
        return (String.valueOf(row) + String.valueOf(col)).hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(row) + String.valueOf(col);
    }

    public static VariableIdentity id(int row, int col) {
        return new VariableIdentity(row, col);
    }
}