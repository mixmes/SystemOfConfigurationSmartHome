package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Notification implements EntityBean  {
    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "deviceID")
    private long deviceID;
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "sender")
    private String sender;
    public Notification() {
    }
    public Notification(String message, Date date, String sender) throws ParseException {
        this.message = message;
        this.date = date;
        this.sender = sender;
    }
    public Notification(long id,String message, Date date, String sender) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.sender = sender;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) throws ParseException {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id && deviceID == that.deviceID && Objects.equals(message, that.message) && Objects.equals(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceID);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", sender='" + sender + '\'' +
                ", deviceId='" + deviceID+'\''+
                '}';
    }
}
