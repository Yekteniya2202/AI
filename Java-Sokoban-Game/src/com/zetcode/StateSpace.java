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

    private PriorityQueue<State> open = new PriorityQueue<>(State::compareTo);

    private Map<State, State> openSet = new HashMap<>(1000000);

    private Map<State, State> closed = new HashMap<>(1000000);


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

    public List<State> getSolution() {
        State initialState = board.getState();
        System.out.println(initialState);
        open.add(initialState);
        openSet.put(initialState, initialState);

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
                List<State> solution = state.getSolutionPath(new ArrayList<>());
                Collections.reverse(solution);
                return solution;
            }

            closed.put(state, state);
            openSet.remove(state);

            List<State> revealedStates = state.revealState();
            revealingCount++;
            revealedStatesCount += revealedStates.size();
            revealedStates.forEach(state1 -> state1.setF(state1.f()));


            for (State childState : revealedStates) {
                State inOpen = openSet.get(childState);
                State inClosed = closed.get(childState);
                if (inOpen == null && inClosed == null) {
                    open.add(childState);
                    openSet.put(childState, childState);
                }
                else if (inOpen != null && childState.getF() < inOpen.getF()) {
                    openSet.get(childState).setF(childState.f());
                    openSet.get(childState).setParent(state);
                }
                else if (inClosed != null && childState.getF() < inClosed.getF()) {
                    closed.remove(childState);
                    openSet.put(childState, childState);
                    openSet.get(childState).setF(childState.f());
                    openSet.get(childState).setParent(state);
                }
            }
        }
        board.setState(initialState);
        return new ArrayList<>();
    }
}
