package ru.sfedu.model;

public class Hygrometer extends Sensor{
    private int humidity;

    public Hygrometer() {
    }

    public Hygrometer(long id, String name, int humidity) {
        super(id, name);
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
