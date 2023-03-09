package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Lamp extends Device{
    @XmlElement(name = "maxBrightness")
    private int maxBrightness;
    @XmlElement(name = "currentBrightness")
    private int currentBrightness=0;

    public Lamp() {
    }

    public Lamp(long id, String name,int maxBrightness) {
        super(id, name);
        this.maxBrightness = maxBrightness;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getMaxBrightness() {
        return maxBrightness;
    }

    public void setMaxBrightness(int maxBrightness) {
        this.maxBrightness = maxBrightness;
    }

    public int getCurrentBrightness() {
        return currentBrightness;
    }

    public void setCurrentBrightness(int currentBrightness) {
        this.currentBrightness = currentBrightness;
    }
    @Override
    public void setSensor(Sensor sensor){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lamp lamp = (Lamp) o;
        return state == lamp.state && maxBrightness == lamp.maxBrightness && currentBrightness == lamp.currentBrightness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), state, maxBrightness, currentBrightness);
    }
}
