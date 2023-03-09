package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Lock extends Device{

    public Lock() {
    }

    public Lock(long id, String name) {
        super(id, name);

    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    @Override
    public void setSensor(Sensor sensor){}

    @Override
    public String toString() {
        return "Lock{" +
                "state=" + state +
                ", id=" + id +
                ", smartHomeId=" + smartHomeId +
                ", name='" + name + '\'' +
                ", sensor=" + sensor +
                ", notifications=" + notifications +
                '}';
    }

}
