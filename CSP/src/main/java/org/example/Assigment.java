package org.example;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Assigment<T> {
    protected Map<Variable<T>, List<Variable<T>>> variablesGraph = new HashMap<>();


    public abstract void fill(String string);

    protected abstract void fillVariables(String string);

    protected abstract void fillConstraints();

    abstract boolean constraintsCheck();

    abstract Assigment<T> createNewAssigment(Map<Variable<T>, List<Variable<T>>> variablesGraph);

    public boolean isCompleted() {
        return variablesGraph.keySet().stream().allMatch(Variable::isAssigned) && constraintsCheck();
    }

    public boolean isConsistent() {
        return constraintsCheck();
    }

    public Assigment<T> assign(Variable<T> variable, T value) {
        Variable<T> assignedVariable = variable.assign(value);
        Map<Variable<T>, List<Variable<T>>> variablesGraphCopy = new HashMap<>(variablesGraph);
        variablesGraphCopy.put(assignedVariable, variablesGraphCopy.get(assignedVariable));
        List<Variable<T>> dependentVariables = variablesGraphCopy.get(variable)
                .stream()
                .filter(variable1 -> !variable1.isAssigned()).toList();
        for (Variable<T> dependentVariable : dependentVariables) {
            dependentVariable.restrict(value);
        }
        return createNewAssigment(variablesGraphCopy);
    }

    public List<Variable<T>> unassignedVariables() {
        return variablesGraph.keySet().stream()
                .filter(variable -> !variable.isAssigned())
                .collect(Collectors.toList());
    }

}
