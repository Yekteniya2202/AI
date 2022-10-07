package com.zetcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.JPanel;

public class Board extends JPanel {

    private final int OFFSET = 30;
    private final int SPACE = 20;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;


    Iterator<State> iteratorOnSolution;

    private ArrayList<Wall> walls;
    private ArrayList<Area> areas;

    private State state;

    private  Stack<Integer> solutionStack;
    private List<State> solutionList;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private int w = 0;
    private int h = 0;

    public boolean isCompleted = false;

    private String[] level = {
             "    ######          \n"
            ,"    ##   #          \n"
            ,"    ##$  #          \n"
            ,"  ####   ##         \n"
            ,"  ##  $   #         \n"
            ,"#### # ## #   ######\n"
            ,"##   # ## #####   .#\n"
            ,"## $          $   .#\n"
            ,"############@##  ..#\n"
            ,"    ##     #########\n"
            ,"    ########        \n"};

//    private String[] level
//            ={"    ######         \n"
//             ,"    ##   #         \n"
//             ,"    ######         \n"
//             ,"  ####   #         \n"
//             ,"  ##  $   #        \n"
//             ,"#### # ## #   #####\n"
//             ,"##   # ## #####  .#\n"
//             ,"## $         $   .#\n"
//             ,"###### ### #@##  .#\n"
//             ,"    ##     ########\n"
//             ,"    ########       \n"};

//    private String[] level
//            = {"##################\n"
//              ,"##         @  $ .#\n"
//              ,"##       $      .#\n"
//              ,"##    $         .#\n"
//              ,"##################\n"};

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        state = new State(this, null, Integer.MIN_VALUE, null);
        initWorld();
        State.startingBaggages = (ArrayList<Baggage>) State.cloneBaggages(this.getState().getBaggs());
        State.areas = areas;
        state.printField();
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }



    private void initWorld() {

        walls = new ArrayList<>();
        areas = new ArrayList<>();

        Wall wall;
        Baggage b;
        Area a;

        int height = level.length;
        int width = level[0].length();

        state.setField(new Cell[height][width - 1]);

        for(int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                char item = level[j].charAt(i);

                switch (item) {
                    case '#':
                        wall = new Wall(j, i, Cell.WALL);
                        walls.add(wall);
                        state.getField()[j][i] = Cell.WALL;
                        break;

                    case '$':
                        b = new Baggage(j, i, Cell.FLOOR);
                        state.getBaggs().add(b);
                        state.getField()[j][i] = Cell.BAGGAGE;
                        break;

                    case '.':
                        a = new Area(j, i, Cell.FLOOR);
                        areas.add(a);
                        state.getField()[j][i] = Cell.AREA;
                        break;

                    case '@':
                        state.setSoko(new Player(j, i, Cell.FLOOR));
                        state.getField()[j][i] = Cell.SOKO;
                        break;

                    case ' ':
                        state.getField()[j][i] = Cell.FLOOR;
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
        world.addAll(state.getBaggs());
        world.add(state.getSoko());

        for (int i = 0; i < world.size(); i++) {

            Actor item = world.get(i);

            if (item instanceof Player || item instanceof Baggage) {

                g.drawImage(item.getImage(), item.widthIdx() * SPACE, item.heightIdx() * SPACE, this);
            } else {

                g.drawImage(item.getImage(), item.widthIdx() * SPACE, item.heightIdx() * SPACE, this);
            }

            if (isCompleted(this.state)) {

                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildWorld(g);
    }

    public boolean canMove(int key) {
        switch (key) {

            case KeyEvent.VK_LEFT:

                if (checkWallCollision(state.getSoko(), LEFT_COLLISION)) {
                    return false;
                }

                if (checkBagCollision(LEFT_COLLISION)) {
                    return false;
                }

                return true;

            case KeyEvent.VK_RIGHT:

                if (checkWallCollision(state.getSoko(), RIGHT_COLLISION)) {
                    return false;
                }

                if (checkBagCollision(RIGHT_COLLISION)) {
                    return false;
                }

                return true;

            case KeyEvent.VK_UP:

                if (checkWallCollision(state.getSoko(), TOP_COLLISION)) {
                    return false;
                }

                if (checkBagCollision(TOP_COLLISION)) {
                    return false;
                }

                return true;

            case KeyEvent.VK_DOWN:

                if (checkWallCollision(state.getSoko(), BOTTOM_COLLISION)) {
                    return false;
                }

                if (checkBagCollision(BOTTOM_COLLISION)) {
                    return false;
                }

                return true;
            default:
                return false;
        }
    }

    public void move(int key) {
        Cell toBeVisited;
        switch (key) {
            case KeyEvent.VK_LEFT:

                if (checkWallCollision(state.getSoko(), LEFT_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(LEFT_COLLISION)) {
                    return;
                }


                toBeVisited = state.getField()[state.getSoko().heightIdx()][state.getSoko().widthIdx() - 1];
                state.getSoko().move(0, -1);
                state.getSoko().setStandsOn(toBeVisited);
                state.updateField();

                break;

            case KeyEvent.VK_RIGHT:

                if (checkWallCollision(state.getSoko(), RIGHT_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(RIGHT_COLLISION)) {
                    return;
                }


                toBeVisited = state.getField()[state.getSoko().heightIdx()][state.getSoko().widthIdx() + 1];
                state.getSoko().move(0, 1);
                state.getSoko().setStandsOn(toBeVisited);
                state.updateField();
                break;

            case KeyEvent.VK_UP:

                if (checkWallCollision(state.getSoko(), TOP_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(TOP_COLLISION)) {
                    return;
                }


                toBeVisited = state.getField()[state.getSoko().heightIdx() - 1][state.getSoko().widthIdx()];
                state.getSoko().move(-1, 0);
                state.getSoko().setStandsOn(toBeVisited);
                state.updateField();
                break;

            case KeyEvent.VK_DOWN:

                if (checkWallCollision(state.getSoko(), BOTTOM_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(BOTTOM_COLLISION)) {
                    return;
                }


                toBeVisited = state.getField()[state.getSoko().heightIdx() + 1][state.getSoko().widthIdx()];
                state.getSoko().move(1, 0);
                state.getSoko().setStandsOn(toBeVisited);
                state.updateField();
                break;

            case KeyEvent.VK_R:

                restartLevel();

                break;

            default:
                break;
        }
    }

    public void addSolution(Stack<Integer> solutionStack) {
        this.solutionStack = solutionStack;
    }

    public void addSolution(List<State> solutionList) {
        this.solutionList = solutionList;
        this.iteratorOnSolution = solutionList.iterator();
        this.state = iteratorOnSolution.next();
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Area> getAreas() {
        return areas;
    }

    public List<State> generateGoalStates() {
        List<State> goalStates = new ArrayList<>();
        ArrayList<Baggage> baggagesOnAreas = new ArrayList<>();
        for(Area area : areas){
            baggagesOnAreas.add(new Baggage(area.heightIdx(), area.widthIdx(), Cell.AREA));
        }

        for(Baggage baggage : baggagesOnAreas){

            if (state.getField()[baggage.heightIdx() - 1][baggage.widthIdx()] == Cell.FLOOR) {
                State newState = new State(this, null, -1, state.getField().clone());
                newState.setBaggs(baggagesOnAreas);
                Player player = new Player(baggage.heightIdx() - 1, baggage.widthIdx(), Cell.FLOOR);
                newState.setSoko(player);
                goalStates.add(newState);
            }
            if (state.getField()[baggage.heightIdx() + 1][baggage.widthIdx()] == Cell.FLOOR) {
                State newState = new State(this, null, -1, state.getField().clone());
                newState.setBaggs(baggagesOnAreas);
                Player player = new Player(baggage.heightIdx() + 1, baggage.widthIdx(), Cell.FLOOR);
                newState.setSoko(player);
                goalStates.add(newState);
            }
            if (state.getField()[baggage.heightIdx()][baggage.widthIdx() - 1] == Cell.FLOOR) {
                State newState = new State(this, null, -1, state.getField().clone());
                newState.setBaggs(baggagesOnAreas);
                Player player = new Player(baggage.heightIdx(), baggage.widthIdx() - 1, Cell.FLOOR);
                newState.setSoko(player);
                goalStates.add(newState);
            }
            if (state.getField()[baggage.heightIdx()][baggage.widthIdx() + 1] == Cell.FLOOR) {
                State newState = new State(this, null, -1, state.getField().clone());
                newState.setBaggs(baggagesOnAreas);
                Player player = new Player(baggage.heightIdx(), baggage.widthIdx() + 1, Cell.FLOOR);
                newState.setSoko(player);
                goalStates.add(newState);
            }
        }
        goalStates.forEach(state1 -> state1.updateField());

        return goalStates;

    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (isCompleted) {
                return;
            }
            int key = e.getKeyCode();
            move(key);
            stepBySolution(key);
            repaint();
            state.printField();
        }
    }

    private void stepBySolution(int key) {
        if (key == KeyEvent.VK_C){
            if (iteratorOnSolution.hasNext()){
                this.state = iteratorOnSolution.next();
            }
        }
    }

    private boolean checkWallCollision(Actor actor, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isLeftCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }

                return false;

            case TOP_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isTopCollision(wall)) {
                        return true;
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isBottomCollision(wall)) {

                        return true;
                    }
                }

                return false;

            default:
                break;
        }

        return false;
    }

    private boolean checkBagCollision(int type) {
        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isLeftCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, LEFT_COLLISION)) {
                                return true;
                            }
                        }
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isRightCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isTopCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isBottomCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, BOTTOM_COLLISION)) {

                                return true;
                            }
                        }
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }
    private boolean checkBagCollisionAndMove(int type) {

        Cell toBeVisited;
        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isLeftCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, LEFT_COLLISION)) {
                                return true;
                            }
                        }


                        toBeVisited = state.getField()[bag.heightIdx()][bag.widthIdx() - 1];
                        bag.move(0, -1);
                        bag.setStandsOn(toBeVisited);
                        state.updateField();
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isRightCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        toBeVisited = state.getField()[bag.heightIdx()][bag.widthIdx() + 1];
                        bag.move(0, 1);
                        bag.setStandsOn(toBeVisited);
                        state.updateField();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isTopCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        toBeVisited = state.getField()[bag.heightIdx() - 1][bag.widthIdx()];
                        bag.move(-1, 0);
                        bag.setStandsOn(toBeVisited);
                        state.updateField();
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < state.getBaggs().size(); i++) {

                    Baggage bag = state.getBaggs().get(i);

                    if (state.getSoko().isBottomCollision(bag)) {

                        for (int j = 0; j < state.getBaggs().size(); j++) {

                            Baggage item = state.getBaggs().get(j);

                            if (!bag.equals(item)) {

                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, BOTTOM_COLLISION)) {

                                return true;
                            }
                        }

                        toBeVisited = state.getField()[bag.heightIdx() + 1][bag.widthIdx()];
                        bag.move(1, 0);
                        bag.setStandsOn(toBeVisited);
                        state.updateField();
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }

//    public void isCompletedWriteField() {
//
//        int nOfBags = state.getBaggs().size();
//        int finishedBags = 0;
//
//        for (int i = 0; i < nOfBags; i++) {
//
//            Baggage bag = state.getBaggs().get(i);
//
//            for (int j = 0; j < nOfBags; j++) {
//
//                Area area = areas.get(j);
//
//                if (bag.x() == area.x() && bag.y() == area.y()) {
//
//                    finishedBags += 1;
//                }
//            }
//        }
//
//        if (finishedBags == nOfBags) {
//
//            isCompleted = true;
//            repaint();
//        }
//    }

    public boolean isCompleted(State checkState) {

        int nOfBags = checkState.getBaggs().size();
        ArrayList<Baggage> baggages = checkState.getBaggs();
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {
            if (baggages.get(i).widthIdx() == areas.get(i).widthIdx() && baggages.get(i).heightIdx() == areas.get(i).heightIdx()){
                finishedBags++;
            }
        }

        if (finishedBags == nOfBags) {

            return true;
        }
        return false;
    }

    private void restartLevel() {

        areas.clear();
        state.getBaggs().clear();
        walls.clear();

        initWorld();

        if (isCompleted) {
            isCompleted = false;
        }
    }
}
