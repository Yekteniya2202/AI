package com.zetcode;

import java.awt.Image;

public class Actor implements Cloneable {

    private final int SPACE = 20;

    private Cell standsOn;
    private int heightIdx;
    private int widthIdx;
    private Image image;

    public Actor(int heightIdx, int widthIdx, Cell standsOn) {
        this.standsOn = standsOn;
        this.heightIdx = heightIdx;
        this.widthIdx = widthIdx;
    }

    public Cell getStandsOn() {
        return standsOn;
    }

    public void setStandsOn(Cell standsOn) {
        this.standsOn = standsOn;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img;
    }

    public int heightIdx() {
        
        return heightIdx;
    }

    public int widthIdx() {
        
        return widthIdx;
    }

    public void setHeightIdx(int heightIdx) {
        
        this.heightIdx = heightIdx;
    }

    public void setWidthIdx(int widthIdx) {
        
        this.widthIdx = widthIdx;
    }

    public boolean isLeftCollision(Actor actor) {
        
        return widthIdx() - 1 == actor.widthIdx() && heightIdx() == actor.heightIdx();
    }

    public boolean isRightCollision(Actor actor) {
        
        return widthIdx() + 1 == actor.widthIdx() && heightIdx() == actor.heightIdx();
    }

    public boolean isTopCollision(Actor actor) {

        boolean col = heightIdx() - 1 == actor.heightIdx() && widthIdx() == actor.widthIdx();
        return heightIdx() - 1 == actor.heightIdx() && widthIdx() == actor.widthIdx();
    }

    public boolean isBottomCollision(Actor actor) {
        
        return heightIdx() + 1 == actor.heightIdx() && widthIdx() == actor.widthIdx();
    }

    @Override
    public Actor clone() {
        return new Actor(this.heightIdx, this.widthIdx, this.standsOn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        Actor actor = (Actor) o;
        return heightIdx == actor.heightIdx && widthIdx == actor.widthIdx;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "x=" + heightIdx +
                ", y=" + widthIdx +
                '}';
    }
}
