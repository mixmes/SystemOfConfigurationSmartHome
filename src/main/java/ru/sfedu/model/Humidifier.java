package ru.sfedu.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

public class Humidifier extends Device{
    private static final Logger log = LogManager.getLogger(Humidifier.class);
    @XmlElement(name = "humidityForOn")
    private int humidityForOn;
    @XmlElement(name = "humidityForOff")
    private int humidityForOff;
    @XmlElement(name = "maxPower")
    private  int maxPower;
    @XmlElement(name = "currentPower")
    private int currentPower=0;
    private boolean state=false;

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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void automaticAction() throws Exception {
        if(humidityForOff==0 && humidityForOn==0){
            log.info("Humidifier does not have humidityOff and humidityOn");
            log.info("Humidifier generate notification");
            if(((Hygrometer)sensor).getHumidity()<30) {
                if(!state) this.generateNotification("Humidity too low. You need to switch on the humidifier");
            }else if(((Termometr)sensor).getTemperature()<25){
                if(state) this.generateNotification("Humidity too hot. You need to switch off the humidifier");
            }
        }
        if (((Hygrometer)sensor).getHumidity()==humidityForOff){
            log.info("Humidifier is off");
            state = false;
            this.generateNotification("The humidity reached "+humidityForOff+"%. Humidifier is off");
        }else if (((Hygrometer)sensor).getHumidity()==humidityForOn){
            log.info("Humidifier is on");
            state = true;
            this.generateNotification("The humidity reached "+humidityForOn+"%. Humidifier is on");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Humidifier that = (Humidifier) o;
        return humidityForOn == that.humidityForOn && humidityForOff == that.humidityForOff && maxPower == that.maxPower && currentPower == that.currentPower && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(humidityForOn, humidityForOff, maxPower, currentPower, state);
    }
}
