package com.zetcode;

import java.awt.EventQueue;
import java.util.List;
import java.util.Stack;
import javax.swing.JFrame;

public class Sokoban extends JFrame {

    private final int OFFSET = 30;

    public Sokoban() {

        initUI();
    }

    private void initUI() {

        Board board = new Board();
        add(board);

        StateSpace stateSpace = new StateSpace(board);
        long time = System.currentTimeMillis();
        List<State> solution = stateSpace.getSolution();
        if (solution.size() != 0) {

            System.out.println("RESULTS");
            solution.forEach(state -> state.getSoko().loadImage());
            solution.forEach(state -> state.getBaggs().forEach(baggage -> baggage.loadImage()));
            System.out.println("Finished for = " + (System.currentTimeMillis() - time) + " millis");
            System.out.println("Iterations = " + stateSpace.iterations);
            System.out.println("Steps count = " + solution.size());
            System.out.println("Revealing count = " + stateSpace.revealingCount);
            System.out.println("Revealed states = " + stateSpace.revealedStatesCount);
            System.out.println("Max states count = " + stateSpace.maxStatesCount);
            System.out.println("Max opened states = " + stateSpace.maxOpenCount);
            System.out.println("Max closed states = " + stateSpace.maxCloseCount);
            board.addSolution(solution);
        }
        setTitle("Sokoban");
        setSize(board.getBoardWidth() + OFFSET,
                board.getBoardHeight() + 2 * OFFSET);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Sokoban game = new Sokoban();
            game.setVisible(true);
        });
    }
}
