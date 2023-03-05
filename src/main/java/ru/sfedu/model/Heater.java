package ru.sfedu.model;

import java.util.List;

public class Heater extends Device{
    private int temperatureForOn;
    private int temperatureForOff;
    private int maxPower;
    private int currentPower;

    public Heater() {
    }

    public Heater(long id, String name, Sensor sensor, List<Notification> notifications, int temperatureForOn, int temperatureForOff, int maxPower, int currentPower) {
        super(id, name, sensor, notifications);
        this.temperatureForOn = temperatureForOn;
        this.temperatureForOff = temperatureForOff;
        this.maxPower = maxPower;
        this.currentPower = currentPower;
    }

    public int getTemperatureForOn() {
        return temperatureForOn;
    }

    public void setTemperatureForOn(int temperatureForOn) {
        this.temperatureForOn = temperatureForOn;
    }

    public int getTemperatureForOff() {
        return temperatureForOff;
    }

    public void setTemperatureForOff(int temperatureForOff) {
        this.temperatureForOff = temperatureForOff;
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
