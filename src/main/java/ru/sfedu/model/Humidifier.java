package ru.sfedu.model;

import java.util.List;

public class Humidifier extends Device{
    private int humidityForOn;
    private int humidityForOff;
    private  int maxPower;
    private int currentPower;

    public Humidifier() {
    }

    public Humidifier(long id, String name, Sensor sensor, List<Notification> notifications, int humidityForOn, int humidityForOff, int maxPower, int currentPower) {
        super(id, name, sensor, notifications);
        this.humidityForOn = humidityForOn;
        this.humidityForOff = humidityForOff;
        this.maxPower = maxPower;
        this.currentPower = currentPower;
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
