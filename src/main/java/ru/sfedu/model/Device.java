package ru.sfedu.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Device implements EntityBean {
    protected long id;
    protected String name;
    protected Sensor sensor;
    protected boolean state=false;
    protected List<Notification> notifications = new ArrayList<>();

    public Device() {
    }

    public Device(long id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    @Override
    public long getId() {
        return this.id;
    }
    public void generateNotification(String message){
        Notification notification = new Notification(message,new Date(),this.getClass().getSimpleName()+". "+name);
        notification.setDeviceID(id);
        notifications.add(notification);
    }
}
