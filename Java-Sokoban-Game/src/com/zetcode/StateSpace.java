package com.zetcode;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class StateSpace {
    private Board board;

    private Stack<State> open = new Stack<>();

    private Set<State> openSet = new HashSet<>(10000000);
    private Set<State> closed = new HashSet<>(10000000);

    private Stack<Integer> solutionPath = new Stack<>();

    public StateSpace(Board board) {
        this.board = board;
    }

    public Stack<Integer> getSolution() {
        State initialState = board.getState();
        System.out.println(initialState);
        open.add(initialState);
        openSet.add(initialState);

        while (open.size() != 0) {
            State state = open.pop();

            if (state.isCompleted()) {
                board.setState(initialState);
                System.out.println(closed.size());
                System.out.println(state);
                return state.getSolutionPath(solutionPath);
            }

            closed.add(state);
            openSet.remove(state);

            List<State> revealedStates = state.revealState();

            for (State childState : revealedStates) {

                if (!openSet.contains(childState) && !closed.contains(childState)) {
                    open.push(childState);
                    openSet.add(childState);
                }
            }
        }
        board.setState(initialState);
        return new Stack<>();
    }
}
