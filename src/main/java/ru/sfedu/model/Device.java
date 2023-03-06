package ru.sfedu.model;
import java.util.ArrayList;
import java.util.List;
public class Device implements EntityBean {
    private long id;
    private String name;
    private Sensor sensor;
    private List<Notification> notifications = new ArrayList<>();

    public Device() {
    }

    public Device(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public long getID() {
        return this.id;
    }
}
