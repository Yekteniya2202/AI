package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuCSP {
    public class SudokuAssigment extends Assigment<Integer> {

        public static SudokuAssigment initialAssigment;
        public SudokuAssigment() {

        }
        private SudokuAssigment(Map<Variable<Integer>, List<Variable<Integer>>> variablesGraph) {
            this.variablesGraph = variablesGraph;
        }

        @Override
        public void fill(String board) {
            fillVariables(board);
            fillConstraints();
            initialAssigment = this;
        }

        @Override
        protected void fillVariables(String board) {
            String[] rows = board.split("\n");
            for (int i = 0; i < 9; i++) {
                char[] chars = rows[i].toCharArray();
                for (int j = 0; j < 9; j++) {
                    Variable<Integer> variable;
                    if (chars[j] == '0') {
                        variable = new Variable<>(IdGenerator.identityOfVariableAt(i, j), initialDomains(), null);
                    } else {
                        Integer parsed = Integer.parseInt(String.valueOf(chars[j]));
                        List<Integer> domains = initialDomains();
                        domains.removeIf(d -> d.equals(parsed));
                        variable = new Variable<>(IdGenerator.identityOfVariableAt(i, j), domains, parsed);
                    }
                    variablesGraph.put(variable, new ArrayList<>());
                }

            }

        }

        @Override
        protected void fillConstraints() {
            //rows + cols
            ArrayList<Variable<Integer>> variables = new ArrayList<>(variablesGraph.keySet());
            for (Variable<Integer> variable : variables) {
                variablesGraph.put(variable, variables.stream()
                        .filter(var ->
                                    var.getIdentity().getCol() == variable.getIdentity().getCol() ||
                                    var.getIdentity().getRow() == variable.getIdentity().getRow())
                        .collect(Collectors.toList()));
            }
        }

        private List<Integer> initialDomains() {
            return new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        }

        @Override
        boolean constraintsCheck() {
            for(Map.Entry<Variable<Integer>, List<Variable<Integer>>> entry : variablesGraph.entrySet()){
                Variable<Integer> variable = entry.getKey();
                if (variable.getValue() != null && entry.getValue().stream().filter(var -> var.getValue() != null).anyMatch(var -> var.getValue().equals(variable.getValue()))){
                    return false;
                }
            }
            return true;
        }

        @Override
        Assigment<Integer> createNewAssigment(Map<Variable<Integer>, List<Variable<Integer>>> variablesGraph) {
            return new SudokuAssigment(variablesGraph);
        }
    }

    public static final String TEST =
            "003020600\n" +
                    "900305001\n" +
                    "001806400\n" +
                    "008102900\n" +
                    "700000008\n" +
                    "006708200\n" +
                    "002609500\n" +
                    "800203009\n" +
                    "005010300";

    private Assigment<Integer> initialAssigment;

    public SudokuCSP() {
        setUpCsp();
    }

    private void setUpCsp() {
        initialAssigment = new SudokuAssigment();
        initialAssigment.fill(TEST);
    }

    public Optional<Assigment<Integer>> solve(){
        return solve(SudokuAssigment.initialAssigment);
    }

    public Optional<Assigment<Integer>> solve(Assigment<Integer> assigment){
        if (assigment.isCompleted()){
            return Optional.of(assigment);
        }

        //take first (no heuristics)
        Variable<Integer> unassignedVariable = assigment.unassignedVariables().get(0);

        return unassignedVariable.getDomain().stream()
                .map(value -> assigment.assign(unassignedVariable, value))
                .filter(Assigment::isConsistent)
                .map(consistentAssigment -> solve(consistentAssigment))
                .findFirst()
                .get();

    }
}
