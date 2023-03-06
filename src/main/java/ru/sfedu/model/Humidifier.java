package ru.sfedu.model;

import java.util.List;

public class Humidifier extends Device{
    private int humidityForOn;
    private int humidityForOff;
    private  int maxPower;
    private int currentPower=0;

    public Humidifier() {
    }

    public Humidifier(long id, String name,int maxPower) {
        super(id, name);
        this.maxPower = maxPower;
    }

    public int getHumidityForOn() {
        return humidityForOn;
    }

    public void setHumidityForOn(int humidityForOn) {
        this.humidityForOn = humidityForOn;
    }

    public int getHumidityForOff() {
        return humidityForOff;
    }

    public void setHumidityForOff(int humidityForOff) {
        this.humidityForOff = humidityForOff;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }
}
