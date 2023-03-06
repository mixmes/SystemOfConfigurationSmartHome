package ru.sfedu.model;

public class Heater extends Device{
    private int temperatureForOn;
    private int temperatureForOff;
    private int maxPower;
    private int currentPower=0;
    private boolean isOn=false;


    public Heater(long id, String name, int maxPower) {
        super(id, name);
        this.maxPower = maxPower;
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

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
