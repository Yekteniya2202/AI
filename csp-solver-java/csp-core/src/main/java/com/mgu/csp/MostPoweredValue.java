package com.mgu.csp;

public class MostPoweredValue<Type> implements VariableOrdering<Type> {
    @Override
    public Variable<Type> selectUnassignedVariable(Assignment<Type> assignment) {
        return VariableOrdering.super.selectUnassignedVariable(assignment);
    }
}
