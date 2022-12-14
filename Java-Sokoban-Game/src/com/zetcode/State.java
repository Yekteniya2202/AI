package com.zetcode;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State implements Comparable {
    private State parent;

    private Board board;

    public int generatedByStep = Integer.MIN_VALUE;

    private ArrayList<Baggage> baggs = new ArrayList<>();

    Cell[][] field;

    public Cell[][] getField() {
        return field;
    }

    public void setField(Cell[][] field) {
        this.field = field;
    }

    private Player soko;

    public State(Board board, State parent, int generatedByStep, Cell[][] field) {
        this.field = field;
        if (field != null) {
            for (int i = 0; i < field.length; i++) {
                this.field[i] = field[i].clone();
            }
        }
        this.board = board;
        this.parent = parent;
        this.generatedByStep = generatedByStep;
    }

    public List<State> revealState() {
        List<State> revealedStates = new ArrayList<>();
        board.setState(this);
        if (board.canMove(KeyEvent.VK_UP) && !isCompleted()) { //38
            State generatedState = new State(board, this, KeyEvent.VK_UP, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_UP);
            revealedStates.add(generatedState);
        }
        board.setState(this);
        if (board.canMove(KeyEvent.VK_DOWN) && !isCompleted()) { //40
            State generatedState = new State(board, this, KeyEvent.VK_DOWN, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_DOWN);
            revealedStates.add(generatedState);
        }
        board.setState(this);
        if (board.canMove(KeyEvent.VK_LEFT) && !isCompleted()) { //37
            State generatedState = new State(board, this, KeyEvent.VK_LEFT, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_LEFT);
            revealedStates.add(generatedState);
        }
        board.setState(this);
        if (board.canMove(KeyEvent.VK_RIGHT) && !isCompleted()) {//39
            State generatedState = new State(board, this, KeyEvent.VK_RIGHT, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_RIGHT);
            revealedStates.add(generatedState);
        }
        return revealedStates;
    }

    public List<State> revealBackState() {
        List<State> revealedStates = new ArrayList<>();
        board.setState(this);
        updateField();

//        System.out.println("REVEALING BACH WITH FIELD");
//        printField();
//        System.out.println("============================================");
//        System.out.println("REVEALING BACH WITH SOKO FREE MOVE");
        //??????????
        if (field[soko.heightIdx()][soko.widthIdx() - 1] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_LEFT);
            revealedStates.add(generatedState);

            //generatedState.printField();
        }
        //????????????
        if (field[soko.heightIdx()][soko.widthIdx() + 1] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_RIGHT);
            revealedStates.add(generatedState);

            //generatedState.printField();
        }
        //????????????
        if (field[soko.heightIdx() - 1][soko.widthIdx()] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_UP);
            revealedStates.add(generatedState);

            //generatedState.printField();
        }
        //??????????
        if (field[soko.heightIdx() + 1][soko.widthIdx()] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            board.setState(generatedState);
            board.move(KeyEvent.VK_DOWN);
            revealedStates.add(generatedState);

            //generatedState.printField();
        }


        //System.out.println("============================================");
        //System.out.println("REVEALING BACH WITH SOKO NEAR THE BAG");
        //??????????????
        //??????????
        if (field[soko.heightIdx()][soko.widthIdx() - 1] == Cell.BAGGAGE) {
            State push = revealBackRightSoko();
            if (push != null) {
                Baggage bag = push.findBaggageByCoord(soko.heightIdx(), soko.widthIdx() - 1);
                bag.move(0, 1);
                push.updateField();
                //push.printField();
                revealedStates.add(push);
            }
        }
        //????????????
        if (field[soko.heightIdx()][soko.widthIdx() + 1] == Cell.BAGGAGE) {

            State push = revealBackLeftSoko();
            if (push != null) {
                Baggage bag = push.findBaggageByCoord(soko.heightIdx(), soko.widthIdx() + 1);
                bag.move(0, -1);
                push.updateField();
                //push.printField();
                revealedStates.add(push);
            }
        }
        //????????????
        if (field[soko.heightIdx() - 1][soko.widthIdx()] == Cell.BAGGAGE) {
            State push = revealBackDownSoko();
            if (push != null) {
                Baggage bag = push.findBaggageByCoord(soko.heightIdx() - 1, soko.widthIdx());
                bag.move(1, 0);
                push.updateField();
                //push.printField();
                revealedStates.add(push);
            }
        }
        //??????????
        if (field[soko.heightIdx() + 1][soko.widthIdx()] == Cell.BAGGAGE) {
            State push = revealBackUpSoko();
            if (push != null) {
                Baggage bag = push.findBaggageByCoord(soko.heightIdx() + 1, soko.widthIdx());
                bag.move(-1, 0);
                push.updateField();
                //push.printField();
                revealedStates.add(push);
            }
        }

        //System.out.println("============================================");
        return revealedStates;

    }

    private Baggage findBaggageByCoord(int h, int w) {
        for (Baggage bag : baggs) {
            if (bag.heightIdx() == h && bag.widthIdx() == w){
                return bag;
            }
        }
        throw new RuntimeException("Finding bag went wrong");
    }

    private State revealBackLeftSoko() {
        if (field[soko.heightIdx()][soko.widthIdx() - 1] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            generatedState.getSoko().move(0, -1);
            generatedState.updateField();
            return generatedState;
        }
        return null;
    }
    private State revealBackRightSoko() {
        if (field[soko.heightIdx()][soko.widthIdx() + 1] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            generatedState.getSoko().move(0, 1);
            generatedState.updateField();
            return generatedState;
        }

        return null;
    }

    private State revealBackUpSoko() {
        if (field[soko.heightIdx() - 1][soko.widthIdx()] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            generatedState.getSoko().move(-1, 0);
            generatedState.updateField();
            return generatedState;
        }

        return null;
    }
    private State revealBackDownSoko() {
        if (field[soko.heightIdx() + 1][soko.widthIdx()] == Cell.FLOOR) {
            State generatedState = new State(board, this, -2, this.field.clone());
            generatedState.setBaggs(cloneBaggages(baggs));
            generatedState.setSoko(soko.clone());
            generatedState.getSoko().move(1, 0);
            generatedState.updateField();
            return generatedState;
        }

        return null;
    }




    public void updateField() {
        for (int j = 0; j < field.length; j++) {
            for (int i = 0; i < field[j].length; i++) {
                field[j][i] = Cell.FLOOR;
            }
        }
        board.getWalls().forEach(wall -> field[wall.heightIdx()][wall.widthIdx()] = Cell.WALL);
        board.getAreas().forEach(area -> field[area.heightIdx()][area.widthIdx()] = Cell.AREA);
        field[soko.heightIdx()][soko.widthIdx()] = Cell.SOKO;
        baggs.forEach(baggage -> field[baggage.heightIdx()][baggage.widthIdx()] = Cell.BAGGAGE);
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
        if (parent == null) {
            return solutionPath;
        }
        return parent.getSolutionPath(solutionPath);
    }

    public List<State> getSolutionPath(List<State> solutionPath) {
        solutionPath.add(this);
        if (parent == null) {
            return solutionPath;
        }
        return parent.getSolutionPath(solutionPath);
    }

    public void printField() {
        for (int j = 0; j < field.length; j++) {
            for (int i = 0; i < field[j].length; i++) {
                System.out.print(field[j][i] + " ");
            }
            System.out.println();
        }
    }


    @Override
    public boolean equals(Object o) {
        State state = (State) o;
        return baggs.equals(state.baggs) && soko.equals(state.soko);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Baggage bag : baggs) {
            hash += bag.heightIdx() + bag.widthIdx();
        }
        hash += soko.heightIdx() + soko.widthIdx();
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
        for (int i = 0; i < baggs.size(); i++) {
            thisSum += baggs.get(i).heightIdx() + baggs.get(i).widthIdx();
            otherSum += state.baggs.get(i).heightIdx() + state.baggs.get(i).widthIdx();
        }
        thisSum += soko.heightIdx() + soko.widthIdx();
        otherSum += state.soko.heightIdx() + state.soko.widthIdx();
        return Integer.compare(thisSum, otherSum);
    }
}
