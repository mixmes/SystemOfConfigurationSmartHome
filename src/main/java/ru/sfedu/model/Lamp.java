package ru.sfedu.model;

import java.util.List;

public class Lamp extends Device{
    private boolean state=false;
    private int maxBrightness;
    private int currentBrightness=0;

    public Lamp() {
    }

    public Lamp(long id, String name,int maxBrightness) {
        super(id, name);
        this.maxBrightness = maxBrightness;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getMaxBrightness() {
        return maxBrightness;
    }

    public void setMaxBrightness(int maxBrightness) {
        this.maxBrightness = maxBrightness;
    }

    public int getCurrentBrightness() {
        return currentBrightness;
    }

    public void setCurrentBrightness(int currentBrightness) {
        this.currentBrightness = currentBrightness;
    }
}
