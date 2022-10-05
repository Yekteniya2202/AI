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

    private Stack<State> open = new Stack<>();

    private Set<State> openSet = new HashSet<>(1000000);

    private Stack<State> openBack = new Stack<>();

    private Set<State> openSetBack = new HashSet<>(1000000);

    private Map<State, State> closed = new HashMap<>(1000000);

    private Map<State, State> closedBack = new HashMap<>(1000000);

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
        openSet.add(initialState);

        List<State> goalStates = board.generateGoalStates();
        goalStates.forEach(state -> openBack.add(state));
        goalStates.forEach(state -> openSetBack.add(state));

        while (open.size() != 0 || openBack.size() != 0) {
            iterations++;
            if (open.size() > maxOpenCount){
                maxOpenCount = open.size();
            }

            if (openBack.size() > maxOpenCount){
                maxOpenCount = openBack.size();
            }

            int sum = open.size() + closed.size() + openBack.size() + closedBack.size();
            if (sum > maxStatesCount){
                maxStatesCount = sum;
            }


            State state = open.pop();

//            if (state.isCompleted()) {
//                board.setState(initialState);
//                System.out.println(closed.size());
//                System.out.println(state);
//                maxCloseCount = closed.size();
//                return state.getSolutionPath(solutionPath);
//            }

            closed.put(state, state);
            openSet.remove(state);

            List<State> revealedStates = state.revealState();
            revealingCount++;
            revealedStatesCount += revealedStates.size();

            for (State childState : revealedStates) {

                if (!openSet.contains(childState) && !closed.containsKey(childState)) {
                    open.push(childState);
                    openSet.add(childState);
                }
            }

            State stateBack = openBack.pop();
            openSetBack.remove(stateBack);
            closedBack.put(stateBack, stateBack);
            //встретились
            //восстановить цепочку
            if (closed.containsKey(stateBack)) {
                maxCloseCount += closed.size() + closedBack.size();
                State forwardEndState = closed.get(stateBack);
                State backwardEndState = stateBack;
                List<State> forwardList = forwardEndState.getSolutionPath(new ArrayList<>());
                List<State> backwardList = backwardEndState.getSolutionPath(new ArrayList<>());

                System.out.println("FORWARD");
                Collections.reverse(forwardList);
                forwardList.remove(forwardList.size() - 1);
                forwardList.forEach(state1 -> state1.printField());

                System.out.println("BACKWARD");
                backwardList.forEach(state1 -> state1.printField());

                forwardList.addAll(backwardList);

                return forwardList;
            }

            List<State> revealedBackStates = stateBack.revealBackState();
            revealingCount++;
            revealedStatesCount += revealedBackStates.size();
            for (State childBackState : revealedBackStates) {

                if (!openSetBack.contains(childBackState) && !closedBack.containsKey(childBackState)) {
                    openBack.push(childBackState);
                    openSetBack.add(childBackState);
                }
            }

        }
        board.setState(initialState);
        return new Stack<>();
    }
}
