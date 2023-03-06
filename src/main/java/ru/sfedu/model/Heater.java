package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@XmlAccessorType(XmlAccessType.FIELD)
public class Heater extends Device{
    private static final Logger log = LogManager.getLogger(Heater.class);
    @XmlElement(name = "temperatureForOn")
    private int temperatureForOn;
    @XmlElement(name = "temperatureForOff")
    private int temperatureForOff;
    @XmlElement(name = "maxPower")
    private int maxPower;
    @XmlElement(name = "currentPower")
    private int currentPower=0;
    @XmlElement(name = "state")
    private boolean state = false;


    public Heater(long id, String name, int maxPower) {
        super(id, name);
        this.maxPower = maxPower;
    }

    public int getTemperatureForOn() {
        return temperatureForOn;
    }

    public void setTemperatureForOn(int temperatureForOn) {
        this.temperatureForOn = temperatureForOn;
    }

    public int getTemperatureForOff() {
        return temperatureForOff;
    }

    public void setTemperatureForOff(int temperatureForOff) {
        this.temperatureForOff = temperatureForOff;
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
        if(temperatureForOff==0 && temperatureForOn==0){
            log.error("Heater does not have temperatureOff and temperatureOn");
            throw new Exception("Automatic activation is not set");
        }
        if (((Termometr)sensor).getTemperature()==temperatureForOff){
            log.info("Heater is off");
            state = false;
        }else if (((Termometr)sensor).getTemperature()==temperatureForOn){
            log.info("Heater is on");
            state = true;
        }
    }
}
