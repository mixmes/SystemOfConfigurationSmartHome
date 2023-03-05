package ru.sfedu.model;

import java.util.List;

public class Socket extends Device {
    private boolean state;

    public Socket() {
    }

    public Socket(long id, String name, Sensor sensor, List<Notification> notifications, boolean state) {
        super(id, name, sensor, notifications);
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
