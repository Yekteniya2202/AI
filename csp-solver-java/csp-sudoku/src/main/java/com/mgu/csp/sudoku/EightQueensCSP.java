package com.mgu.csp.sudoku;

import com.mgu.csp.Assignment;
import com.mgu.csp.CSP;
import com.mgu.csp.Constraint;

import java.util.Set;

public class EightQueensCSP extends CSP<Boolean> {
    @Override
    protected Assignment<Boolean> initialAssignment() {
        return null;
    }

    @Override
    protected Set<Constraint> constraints() {
        return null;
    }
}
