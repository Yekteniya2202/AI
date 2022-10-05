package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Baggage extends Actor {

    public Baggage(int heightIdx, int widthIdx, Cell standsOn ) {
        super(heightIdx, widthIdx, standsOn);

        initBaggage();
    }

    public Baggage(int heightIdx, int widthIdx, boolean imageNeeded, Cell standsOn) {
        super(heightIdx, widthIdx, standsOn);

        if (imageNeeded)
            initBaggage();
    }

    private void initBaggage() {

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
        return new Baggage(super.heightIdx(), super.widthIdx(), super.getStandsOn());
    }
}
