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

}
