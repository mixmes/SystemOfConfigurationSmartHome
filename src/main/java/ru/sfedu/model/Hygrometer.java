package ru.sfedu.model;

import ru.sfedu.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Hygrometer extends Sensor{
    @XmlElement(name = "humidity")
    private int humidity;

    public Hygrometer() {
    }

    public Hygrometer(long id, String name, int humidity) {
        super(id, name, Constants.SensorType.HYGROMETER);
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
