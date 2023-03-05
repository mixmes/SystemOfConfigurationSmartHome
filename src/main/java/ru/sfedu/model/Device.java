package ru.sfedu.model;
import java.util.List;
public class Device {
    private long id;
    private String name;
    private Sensor sensor;
    private List<Notification> notifications;

    public Device() {
    }

    public Device(long id, String name, Sensor sensor, List<Notification> notifications) {
        this.id = id;
        this.name = name;
        this.sensor = sensor;
        this.notifications = notifications;
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
}
