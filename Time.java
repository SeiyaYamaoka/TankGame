package com.example.tankdedone;

public class Time {
    private long start;
    private boolean used = false;
    private boolean isstop = false;

    private long now;
    private long stopms;

    public void Start(){
        if(isstop == false) {
            start = System.currentTimeMillis();
            used = true;
        }else{
            start = System.currentTimeMillis() - stopms;
            isstop = false;
            stopms = 0;
        }
    }

    public void End(){
        used = false;
        start = 0;
        stopms = 0;
    }

    public void Stop(){
        isstop = true;
        long end = System.currentTimeMillis();
        stopms = end - start;


    }

    public boolean Use(){
        return used;
    }

    public long Getms(){
        long end = System.currentTimeMillis();

        return (end - start);
    }

}
