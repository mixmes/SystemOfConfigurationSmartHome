package ru.sfedu.model;

import java.util.List;

public class Lamp extends Device{
    private boolean state;
    private int maxBrightness;
    private int currentBrightness;

    public Lamp() {
    }

    public Lamp(long id, String name, Sensor sensor, List<Notification> notifications, boolean state, int maxBrightness, int currentBrightness) {
        super(id, name, sensor, notifications);
        this.state = state;
        this.maxBrightness = maxBrightness;
        this.currentBrightness = currentBrightness;
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
