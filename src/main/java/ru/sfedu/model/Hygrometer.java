package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Hygrometer extends Sensor{
    private static final Logger log = LogManager.getLogger(Hygrometer.class);
    @XmlElement(name="humidity")
    private int humidity;

    public Hygrometer() {
    }

    public Hygrometer(long id, String name) {
        super(id, name, Constants.SensorType.HYGROMETER);
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void notifyHumidifierOfChanges(Humidifier humidifier, int humidity) throws Exception {
        if(humidifier.getSensor() == null){
            log.error("Humidifier "+this.name+" does not have sensor");
            throw new Exception("This device does not have a sensor");
        }
        if(humidifier.getSensor().equals(this)) {
            this.humidity = humidity;
            humidifier.automaticAction();
        }else{
            log.error("Hygrometer "+name+" not connected to the humidifier "+humidifier.getName());
            throw new Exception("Sensor not connected to this humidifier");
        }
    }
}
