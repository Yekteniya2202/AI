package com.zetcode;

public class Stopwatch {
    private long start;

    public void start(){
        start = System.nanoTime();
    }

    public void reset(){
        start();
    }

    public long end(){
        return System.nanoTime() - start;
    }

    public void printMillisPassed(String stringMark){
        System.out.println("Stopwatch: " + end() + " nano passed at mark " + stringMark);
    }
}
