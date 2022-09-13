package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player extends Actor {

    public Player(int x, int y) {
        super(x, y);

        initPlayer();
    }

    public Player(int x, int y, boolean imageNeeded) {
        super(x, y);

        if (imageNeeded)
            initPlayer();
    }


    private void initPlayer() {

        ImageIcon iicon = new ImageIcon("src/resources/sokoban.png");
        Image image = iicon.getImage();
        setImage(image);
    }

    public void move(int x, int y) {

        int dx = x() + x;
        int dy = y() + y;
        
        setX(dx);
        setY(dy);
    }

    @Override
    public String toString() {
        return "Player{" +
                "x= " + super.x() +
                ", y= " + super.y() +
                "}";
    }

    @Override
    public Player clone() {
        return new Player(super.x(), super.y(), false);
    }

}
