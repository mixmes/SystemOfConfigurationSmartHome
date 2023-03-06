package ru.sfedu.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.XmlElement;

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
            log.error("Humidifier does not have humidityOff and humidityOn");
            throw new Exception("Automatic activation is not set");
        }
        if (((Hygrometer)sensor).getHumidity()==humidityForOff){
            log.info("Humidifier is off");
            state = false;
        }else if (((Hygrometer)sensor).getHumidity()==humidityForOn){
            log.info("Humidifier is on");
            state = true;
        }
    }
}
