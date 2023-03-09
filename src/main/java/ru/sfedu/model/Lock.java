package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
public class Lock extends Device{
    @XmlElement(name = "state")
    private boolean state=true;

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
}
