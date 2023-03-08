package ru.sfedu.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

<<<<<<<<< Temporary merge branch 1
=========

@XmlAccessorType(XmlAccessType.FIELD)
>>>>>>>>> Temporary merge branch 2
public class Device implements EntityBean {

    protected long id;

    protected String name;
    @XmlTransient
    protected Sensor sensor;

    protected boolean state=false;
    @XmlTransient
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
        if(sensor != null) sensor.setDeviceId(id);

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

<<<<<<<<< Temporary merge branch 1
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id == device.id && state == device.state && Objects.equals(name, device.name) && Objects.equals(sensor, device.sensor) && Objects.equals(notifications, device.notifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sensor, state, notifications);
    }
=========

>>>>>>>>> Temporary merge branch 2
}
