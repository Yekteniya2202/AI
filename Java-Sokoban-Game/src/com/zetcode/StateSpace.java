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

        long avgPolling = 0, avgComplete = 0, avgSwapping = 0, avgRevealing = 0, avgFCalc = 0, avgRevealingAStar = 0;
        Stopwatch stopwatch = new Stopwatch();
        while (open.size() != 0) {
            iterations++;
            if (open.size() > maxOpenCount){
                maxOpenCount = open.size();
            }

            int sum = open.size() + closed.size();
            if (sum > maxStatesCount){
                maxStatesCount = sum;
            }


            stopwatch.reset();
            State state = open.poll();
            avgPolling += stopwatch.end();

            stopwatch.reset();
            if (state.isCompleted()) {

                System.out.println(avgPolling);
                System.out.println(avgComplete);
                System.out.println(avgSwapping);
                System.out.println(avgRevealing);
                System.out.println(avgFCalc);
                System.out.println(avgRevealingAStar);

                board.setState(initialState);
                maxCloseCount = closed.size();
                List<State> solution = state.getSolutionPath(new ArrayList<>());
                Collections.reverse(solution);
                return solution;
            }
            avgComplete += stopwatch.end();

            stopwatch.reset();
            closed.put(state, state);
            openSet.remove(state);
            avgSwapping += stopwatch.end();


            stopwatch.reset();
            List<State> revealedStates = state.revealState();
            avgRevealing += stopwatch.end();


            revealingCount++;
            revealedStatesCount += revealedStates.size();

            stopwatch.reset();
            revealedStates.forEach(state1 -> state1.setF(state1.f()));
            avgFCalc += stopwatch.end();

            stopwatch.reset();
            for (State childState : revealedStates) {
                State inOpen = openSet.get(childState);
                State inClosed = closed.get(childState);
                if (inOpen == null && inClosed == null) {
                    open.add(childState);
                    openSet.put(childState, childState);
                }
                else if (inOpen != null
                        && childState.getF() < inOpen.getF()) {
                    inOpen.setF(childState.getF());
                    inOpen.setParent(state);
                }
                else if (inClosed != null
                        && childState.getF() < inClosed.getF()) {
                    closed.remove(inClosed);
                    open.add(inClosed);
                    openSet.put(inClosed, inClosed);
                    inClosed.setF(childState.getF());
                    inClosed.setParent(state);
                }
            }
            avgRevealingAStar += stopwatch.end();
        }

        board.setState(initialState);
        return new ArrayList<>();
    }
}
