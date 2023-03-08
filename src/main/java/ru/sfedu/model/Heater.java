package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Device.class)
public class Heater extends Device{
    @XmlTransient
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

    public Heater(){}
    public Heater(long id, String name, int maxPower) {
        super(id,name);
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
            log.info("Heater does not have temperatureOff and temperatureOn");
            log.info("Heater generate notofocation");
            if(((Termometr)sensor).getTemperature()<15) {
                if(!state) this.generateNotification("Temperature too low. You need to switch on the heater");
            }else if(((Termometr)sensor).getTemperature()<25){
                if(state) this.generateNotification("Temperature too hot. You need to switch off the heater");
            }
        }

        if (((Termometr)sensor).getTemperature()==temperatureForOff){
            log.info("Heater is off");
            state = false;
            this.generateNotification("The temperature reached "+temperatureForOff+" degrees. Heater is off");
        }else if (((Termometr)sensor).getTemperature()==temperatureForOn){
            log.info("Heater is on");
            state = true;
            this.generateNotification("The temperature reached "+temperatureForOff+" degrees. Heater is on");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Heater heater = (Heater) o;
        return temperatureForOn == heater.temperatureForOn && temperatureForOff == heater.temperatureForOff && maxPower == heater.maxPower && currentPower == heater.currentPower && state == heater.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperatureForOn, temperatureForOff, maxPower, currentPower, state);
    }
}
