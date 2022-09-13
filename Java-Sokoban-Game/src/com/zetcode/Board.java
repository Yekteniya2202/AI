package com.zetcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JPanel;

public class Board extends JPanel {

    private final int OFFSET = 30;
    private final int SPACE = 20;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;

    private ArrayList<Wall> walls;
    private ArrayList<Area> areas;

    private State state;

    private  Stack<Integer> solutionStack;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private int w = 0;
    private int h = 0;

    public boolean isCompleted = false;

    private String level
            = "    ######\n"
            + "    ##   #\n"
            + "    ##$  #\n"
            + "  ####   ##\n"
            + "  ##  $   #\n"
            + "#### # ## #   ######\n"
            + "##   # ## #####   .#\n"
            + "## $              .#\n"
            + "###### ### #@##   .#\n"
            + "    ##     #########\n"
            + "    ########\n";

//    private String level
//            = "    ######\n"
//            + "    ##   #\n"
//            + "    ######\n"
//            + "  ####  ##\n"
//            + "  ##  $   #\n"
//            + "#### # ## #   #####\n"
//            + "##   # ## #####  .#\n"
//            + "## $  $          .#\n"
//            + "###### ### #@##  .#\n"
//            + "    ##     ########\n"
//            + "    ########\n";

//    private String level
//            = "##################\n"
//            + "##         @  $ .#\n"
//            + "############  $ .#\n"
//            + "##         #  $ .#\n"
//            + "##         #  $ .#\n"
//            + "##################\n";

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        state = new State(this, null, Integer.MIN_VALUE);
        initWorld();
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

        int x = OFFSET;
        int y = OFFSET;

        Wall wall;
        Baggage b;
        Area a;

        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

            switch (item) {

                case '\n':
                    y += SPACE;

                    if (this.w < x) {
                        this.w = x;
                    }

                    x = OFFSET;
                    break;

                case '#':
                    wall = new Wall(x, y);
                    walls.add(wall);
                    x += SPACE;
                    break;

                case '$':
                    b = new Baggage(x, y);
                    state.getBaggs().add(b);
                    x += SPACE;
                    break;

                case '.':
                    a = new Area(x, y);
                    areas.add(a);
                    x += SPACE;
                    break;

                case '@':
                    state.setSoko(new Player(x, y));
                    x += SPACE;
                    break;

                case ' ':
                    x += SPACE;
                    break;

                default:
                    break;
            }

            h = y;
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

                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
            } else {

                g.drawImage(item.getImage(), item.x(), item.y(), this);
            }

            if (isCompleted) {

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
        switch (key) {

            case KeyEvent.VK_LEFT:

                if (checkWallCollision(state.getSoko(), LEFT_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(LEFT_COLLISION)) {
                    return;
                }

                state.getSoko().move(-SPACE, 0);

                break;

            case KeyEvent.VK_RIGHT:

                if (checkWallCollision(state.getSoko(), RIGHT_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(RIGHT_COLLISION)) {
                    return;
                }

                state.getSoko().move(SPACE, 0);

                break;

            case KeyEvent.VK_UP:

                if (checkWallCollision(state.getSoko(), TOP_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(TOP_COLLISION)) {
                    return;
                }

                state.getSoko().move(0, -SPACE);

                break;

            case KeyEvent.VK_DOWN:

                if (checkWallCollision(state.getSoko(), BOTTOM_COLLISION)) {
                    return;
                }

                if (checkBagCollisionAndMove(BOTTOM_COLLISION)) {
                    return;
                }

                state.getSoko().move(0, SPACE);

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
        }
    }

    private void stepBySolution(int key) {
        if (key == KeyEvent.VK_C){
            if (!solutionStack.empty()){
                move(solutionStack.pop());
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

                        bag.move(-SPACE, 0);
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

                        bag.move(SPACE, 0);
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

                        bag.move(0, -SPACE);
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

                        bag.move(0, SPACE);
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
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {

            Baggage bag = checkState.getBaggs().get(i);

            for (int j = 0; j < nOfBags; j++) {

                Area area = areas.get(j);

                if (bag.x() == area.x() && bag.y() == area.y()) {

                    finishedBags += 1;
                }
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
