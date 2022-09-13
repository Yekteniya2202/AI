package com.zetcode;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class State implements Comparable {
    private State parent;

    private Board board;

    public int generatedByStep = Integer.MIN_VALUE;

    private ArrayList<Baggage> baggs = new ArrayList<>();

    private Player soko;

    public State(Board board, State parent, int generatedByStep) {
        this.board = board;
        this.parent = parent;
        this.generatedByStep = generatedByStep;
    }

    public List<State> revealState(){
        List<State> revealedStates = new ArrayList<>();
        board.setState(this);
        if (board.canMove(KeyEvent.VK_UP) && !isCompleted()){ //38
            State generatedState = new State(board, this, KeyEvent.VK_UP);
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_UP);
            revealedStates.add(generatedState);
        }
        board.setState(this);
        if (board.canMove(KeyEvent.VK_DOWN) && !isCompleted()){ //40
            State generatedState = new State(board, this, KeyEvent.VK_DOWN);
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_DOWN);
            revealedStates.add(generatedState);
        }
        board.setState(this);
        if (board.canMove(KeyEvent.VK_LEFT) && !isCompleted()){ //37
            State generatedState = new State(board, this, KeyEvent.VK_LEFT);
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_LEFT);
            revealedStates.add(generatedState);
        }
        board.setState(this);
        if (board.canMove(KeyEvent.VK_RIGHT) && !isCompleted()){//39
            State generatedState = new State(board, this, KeyEvent.VK_RIGHT);
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_RIGHT);
            revealedStates.add(generatedState);
        }
        return revealedStates;
    }
    public static ArrayList<Baggage> cloneBaggages(ArrayList<Baggage> list) {
        ArrayList<Baggage> clone = new ArrayList<Baggage>(list.size());
        for (Baggage item : list) clone.add(item.clone());
        return clone;
    }
    public boolean isCompleted() {
        return board.isCompleted(this);
    }
    public ArrayList<Baggage> getBaggs() {
        return baggs;
    }

    public void setBaggs(ArrayList<Baggage> baggs) {
        this.baggs = baggs;
    }

    public Player getSoko() {
        return soko;
    }

    public void setSoko(Player soko) {
        this.soko = soko;
    }


    public Stack<Integer> getSolutionPath(Stack<Integer> solutionPath) {
        solutionPath.push(this.generatedByStep);
        if(parent == null){
            return solutionPath;
        }
        return parent.getSolutionPath(solutionPath);
    }

    @Override
    public boolean equals(Object o) {
        State state = (State) o;
        return baggs.equals(state.baggs) && soko.equals(state.soko);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(Baggage bag: baggs){
            hash += bag.x() + bag.y();
        }
        hash += soko.x() + soko.y();
        return hash;
    }

    @Override
    public String toString() {
        return "State{ " +
                "baggs= " + baggs +
                ", soko=" + soko +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        State state = (State) o;
        int thisSum = 0;
        int otherSum = 0;
        for(int i = 0; i < baggs.size(); i++){
            thisSum += baggs.get(i).x() + baggs.get(i).y();
            otherSum += state.baggs.get(i).x() + state.baggs.get(i).y();
        }
        thisSum += soko.x() + soko.y();
        otherSum += state.soko.x() + state.soko.y();
        return Integer.compare(thisSum, otherSum);
    }
}
