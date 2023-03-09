package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Lock extends Device{
    @XmlElement(name = "state")
    private boolean state=false;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lock lock = (Lock) o;
        return state == lock.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), state);
    }
}
