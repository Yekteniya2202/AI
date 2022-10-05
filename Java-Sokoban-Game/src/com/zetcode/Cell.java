package com.zetcode;

public enum Cell {
    WALL('#'),
    FLOOR(' '),
    AREA('.'),
    BAGGAGE('$'),
    SOKO('@');

    char code;
    Cell(char code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return "" + code;
    }

}
