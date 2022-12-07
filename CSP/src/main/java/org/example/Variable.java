package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Variable<T> {
    private VariableIdentity identity;
    private List<T> domain;
    private T value;

    public T getValue() {
        return value;
    }

    public List<T> getDomain() {
        return domain;
    }

    public Variable(VariableIdentity variableIdentity, List<T> domain, T value){
        this.identity = variableIdentity;
        this.domain = domain;
        this.value = value;
    }

    public VariableIdentity getIdentity() {
        return identity;
    }

    public boolean isAssigned() {
        return value != null;
    }

    public Variable<T> assign(T value){
        if (!domain.contains(value)){
            throw new IllegalStateException("Value " + value + " is not in domain list " + domain);
        }

        return new Variable<>(identity, new ArrayList<>(), value);
    }

    public Variable<T> restrict(T restrictByValue) {
        if (isAssigned()) {
            throw new IllegalStateException("Unable to restrict domain values since the variable has already been " +
                    "assigned a value.");
        }
        if (!this.domain.contains(restrictByValue)) {
            // do nothing, since we already are in converged state
            return this;
        }
        final List<T> shallowCopyOfDomain = new ArrayList<>(domain);
        shallowCopyOfDomain.remove(restrictByValue);
        return new Variable<>(identity, shallowCopyOfDomain, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable<?> variable = (Variable<?>) o;

        return identity.equals(variable.identity);
    }

    @Override
    public int hashCode() {
        return identity.hashCode();
    }

}
