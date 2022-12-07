package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Baggage extends Actor {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Baggage(int heightIdx, int widthIdx, Cell standsOn) {
        super(heightIdx, widthIdx, standsOn);

        initBaggage();
    }

    public Baggage(int heightIdx, int widthIdx, boolean imageNeeded, Cell standsOn) {
        super(heightIdx, widthIdx, standsOn);

        if (imageNeeded)
            initBaggage();
    }

    private void initBaggage() {

        loadImage();
    }

    public void loadImage(){
        ImageIcon iicon = new ImageIcon("src/resources/baggage.png");
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
        return "Baggage{" +
                "x= " + super.heightIdx() +
                ", y= " + super.widthIdx() +
                "}";
    }

    @Override
    public Baggage clone() {
        Baggage newBaggage = new Baggage(super.heightIdx(), super.widthIdx(), false, super.getStandsOn());
        newBaggage.setId(this.id);
        return newBaggage;
    }
}
