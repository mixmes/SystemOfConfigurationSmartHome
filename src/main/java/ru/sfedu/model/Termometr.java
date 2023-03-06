package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Termometr extends Sensor {
    private static final Logger log = LogManager.getLogger(Termometr.class);
    private int temperature;

    public Termometr() {
    }

    public Termometr(long id, String name, int temperature) {
        super(id, name, Constants.SensorType.TERMOMETR);
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public void notifyHeaterOfChanges(Heater heater, int temperature) throws Exception {
        if(heater.getSensor() == null){
            log.error("Heater "+this.name+" does not have sensor");
            throw new Exception("This device does not have a sensor");
        }
        if(heater.getSensor().equals(this)) {
            this.temperature = temperature;
            heater.automaticAction();
        }else{
            log.error("Termometr "+name+" not connected to the heater"+heater.getName());
            throw new Exception("Sensor not connected to this humidifier");
        }
    }
}
