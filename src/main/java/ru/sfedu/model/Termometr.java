package ru.sfedu.model;

import ru.sfedu.Constants;

public class Termometr extends Sensor {
    private int temperature;

    public Termometr() {
    }

    public Termometr(long id, String name, int temperature) {
        super(id, name, Constants.SensorType.TERMOMETR);
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
