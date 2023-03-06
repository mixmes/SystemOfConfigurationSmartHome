package ru.sfedu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
public class Humidifier extends Device{
    @XmlElement(name = "humidityForOn")
    private int humidityForOn;
    @XmlElement(name = "humidityForOff")
    private int humidityForOff;
    @XmlElement(name = "maxPower")
    private  int maxPower;
    @XmlElement(name = "currentPower")
    private int currentPower=0;

    public Humidifier() {
    }

    public Humidifier(long id, String name,int maxPower) {
        super(id, name);
        this.maxPower = maxPower;
    }

    public int getHumidityForOn() {
        return humidityForOn;
    }

    public void setHumidityForOn(int humidityForOn) {
        this.humidityForOn = humidityForOn;
    }

    public int getHumidityForOff() {
        return humidityForOff;
    }

    public void setHumidityForOff(int humidityForOff) {
        this.humidityForOff = humidityForOff;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }
}
