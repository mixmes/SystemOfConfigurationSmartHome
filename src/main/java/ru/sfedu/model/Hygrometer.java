package ru.sfedu.model;

import ru.sfedu.Constants;

public class Hygrometer extends Sensor{
    private int humidity;

    public Hygrometer() {
    }

    public Hygrometer(long id, String name, int humidity) {
        super(id, name, Constants.SensorType.HYGROMETER);
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
