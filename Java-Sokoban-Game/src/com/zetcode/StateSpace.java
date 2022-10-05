package com.zetcode;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/*
кол-во итераций цикла поиска +
кол-во операций раскрытия узлов +
кол-во раскрываний +
кол-во раскрытых на итерации +
время поиска (мс) (пропорционально итераций)
кол-во шагов в решении
максимальный объем памяти (кол-во узлов)


 */
public class StateSpace {
    private Board board;

    private Queue<State> open = new ArrayDeque<>();

    private Set<State> openSet = new HashSet<>(10000000);
    private Set<State> closed = new HashSet<>(10000000);

    private Stack<Integer> solutionPath = new Stack<>();


    public int iterations = 0;
    public int revealingCount = 0;
    public int revealedStatesCount = 0;
    public int maxStatesCount = 0;

    public int maxOpenCount = 0;
    public int maxCloseCount = 0;

    public StateSpace(Board board) {
        this.board = board;
    }

    public Stack<Integer> getSolution() {
        State initialState = board.getState();
        System.out.println(initialState);
        open.add(initialState);
        openSet.add(initialState);

        List<State> goalStates = board.generateGoalStates();


        while (open.size() != 0) {
            iterations++;
            if (open.size() > maxOpenCount){
                maxOpenCount = open.size();
            }

            int sum = open.size() + closed.size();
            if (sum > maxStatesCount){
                maxStatesCount = sum;
            }


            State state = open.poll();

            if (state.isCompleted()) {
                board.setState(initialState);
                System.out.println(closed.size());
                System.out.println(state);
                maxCloseCount = closed.size();
                return state.getSolutionPath(solutionPath);
            }

            closed.add(state);
            openSet.remove(state);

            List<State> revealedStates = state.revealState();
            revealingCount++;
            revealedStatesCount += revealedStates.size();

            for (State childState : revealedStates) {

                if (!openSet.contains(childState) && !closed.contains(childState)) {
                    open.add(childState);
                    openSet.add(childState);
                }
            }
        }
        board.setState(initialState);
        return new Stack<>();
    }
}
