package ru.sfedu.model;

import ru.sfedu.Constants;

import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

public class Sensor {
    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "deviceID")
    private long deviceID;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "sensorType")
    private Constants.SensorType sensorType;
    public Sensor(){

    }
    public Sensor(long id, String name, Constants.SensorType type){
        this.id = id;
        this.name = name;
        sensorType=type;
    }

    public long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return id == sensor.id && Objects.equals(name, sensor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
