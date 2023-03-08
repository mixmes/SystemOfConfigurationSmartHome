package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
public class Socket extends Device {
    @XmlElement(name = "state")
    private boolean state=false;

    public Socket() {
    }

    public Socket(long id, String name) {
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
}
