package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player extends Actor {

    public Player(int heightIdx, int widthIdx, Cell standsOn) {
        super(heightIdx, widthIdx, standsOn);

        initPlayer();
    }

    public Player(int heightIdx, int widthIdx, boolean imageNeeded,  Cell standsOn) {
        super(heightIdx, widthIdx, standsOn);

        if (imageNeeded)
            initPlayer();
    }


    private void initPlayer() {

        ImageIcon iicon = new ImageIcon("src/resources/sokoban.png");
        Image image = iicon.getImage();
        setImage(image);
    }

    public void move(int heightIdx, int widthIdx) {

        int dx = heightIdx() + heightIdx;
        int dy = widthIdx() + widthIdx;
        
        setHeightIdx(dx);
        setWidthIdx(dy);
    }

    @Override
    public String toString() {
        return "Player{" +
                "x= " + super.heightIdx() +
                ", y= " + super.widthIdx() +
                "}";
    }

    @Override
    public Player clone() {
        return new Player(super.heightIdx(), super.widthIdx(), false, super.getStandsOn());
    }

}
