package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@XmlAccessorType(XmlAccessType.FIELD)
public class User implements EntityBean  {
    @XmlElement(name = "name")
    private String name ;
    @XmlElement(name = "id")
    private long id;
    @XmlTransient
    private SmartHome smartHome;
    @XmlElement(name = "accessLevel")
    private Constants.AcessLevel accessLevel=Constants.AcessLevel.ADMIN;
    @XmlTransient
    private Logger log =  LogManager.getLogger(User.class);

    public User() {
    }

    public User(long id, String name, Constants.AcessLevel level) {
        this.name = name;
        this.id = id;
        accessLevel = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Constants.AcessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Constants.AcessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public SmartHome getSmartHome() {
        return smartHome;
    }

    public void setSmartHome(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public long getID() {
        return this.id;
    }
    public void addDeviceToSmartHome(Device device) throws Exception {
        if (!smartHome.getDevices().contains(device)) {
            smartHome.addDevice(device);
            log.info("User " + name + " add new device "+device.getName()+" to home " + smartHome.getName());
        }else{
            log.error("Device "+device.getName()+" has already added to home "+smartHome.getName());
            throw new Exception("This device has already added to the home");
        }
    }

    public void addResidentToSmartHome(User user) throws Exception {
        if(this.accessLevel==Constants.AcessLevel.ADMIN){
            user.setSmartHome(smartHome);
            user.setAccessLevel(Constants.AcessLevel.RESIDENT);
        }else {
            log.error("The user does not have enough rights to add user" + user.getName());
            throw new Exception("Insufficient rights to add residents");
        }
    }
    public List<Notification> checkSmartHomesNotification(){
        List<Notification> notifications = new ArrayList<>();
        smartHome.getDevices().forEach(d->notifications.addAll(d.getNotifications()));
        return notifications;
    }

    public void changeStateLock(Lock l) throws Exception {
        Device lock = checkDeviceInSmartHome(l);
        boolean changesState = !((Lock)getDeviceFromSmartHome(lock)).isState();
        ((Lock)getDeviceFromSmartHome(lock)).setState(changesState);
        log.info("User "+name+" changes lock state "+l.getName()+" to "+((Lock)getDeviceFromSmartHome(lock)).isState());

    }

    public void changeHeatersPower(Heater h, int power) throws Exception {
        Device heater = checkDeviceInSmartHome(h);
        if (h.getMaxPower() >= power) {
            ((Heater) getDeviceFromSmartHome(heater)).setCurrentPower(power);
        } else {
            log.error("Max power+"+h.getMaxPower()+" < transmited power");
            throw new Exception("Power is higher than allowed");
        }
    }

    public void automateWorkHeater(Heater h,int tempOn, int tempOff) throws Exception {
        Device heat = checkDeviceInSmartHome(h);
        ((Heater)getDeviceFromSmartHome(h)).setTemperatureForOn(tempOn);
        ((Heater)getDeviceFromSmartHome(h)).setTemperatureForOff(tempOff);
    }
    public void changeStateSocket(Socket s) throws Exception {
        Device socket=checkDeviceInSmartHome(s);
        boolean changesState = !((Socket)getDeviceFromSmartHome(socket)).isState();
        ((Socket)getDeviceFromSmartHome(socket)).setState(changesState);

    }
    public void changeHumidifierPower(Humidifier h,int power) throws Exception {
        Device humidifier = checkDeviceInSmartHome(h);
        if(((Humidifier)humidifier).getMaxPower()>=power){
            ((Humidifier)getDeviceFromSmartHome(humidifier)).setCurrentPower(power);
        }else {
            log.error("Max power "+h.getMaxPower()+" < transmitted power "+power);
            throw new Exception("Power is higher than allowed");
        }
    }
    public void automateWorkHumidifier(Humidifier h, int humOn, int humOff) throws Exception {
        if((humOn<=100) && (humOff<=100)) {
            Device humidifier = checkDeviceInSmartHome(h);
            ((Humidifier) getDeviceFromSmartHome(humidifier)).setHumidityForOn(humOn);
            ((Humidifier) getDeviceFromSmartHome(humidifier)).setHumidityForOff(humOff);
        }else{
            log.error("Humidity cannot be higher than 100%. humOn="+humOn+" humOff="+humOff);
            throw new Exception("Humidity cannot be higher than 100%");
        }
    }
    public void changeStateLamp(Lamp l) throws Exception {
        Device lamp = checkDeviceInSmartHome(l);
        boolean changesState = !((Lamp)getDeviceFromSmartHome(lamp)).isState();
        ((Lamp)getDeviceFromSmartHome(lamp)).setState(changesState);
    }

    public void changeLampBrightness(Lamp l,int brightness) throws Exception {
        Device lamp = checkDeviceInSmartHome(l);
        if(l.getMaxBrightness()>=brightness){
            ((Lamp)getDeviceFromSmartHome(lamp)).setCurrentBrightness(brightness);
        }else{
            log.error("Max brightness "+l.getMaxBrightness()+" < transmitted brightness "+brightness);
            throw new Exception("Invalid brightness value");
        }
    }
    private Device checkDeviceInSmartHome(Device dev) throws Exception {
        Optional<Device> device = smartHome.getDevices().stream().filter(d -> d.equals(dev)).findFirst();
        if(device.isPresent()){
            return device.get();
        }else{
            log.error("Device "+dev.getName()+" does not belong to the home "+smartHome.getName());
            throw new Exception("Device does not belong to the users home");
        }
    }

    private Device getDeviceFromSmartHome(Device d){
        return  smartHome.getDevices().get(smartHome.getDevices().indexOf(d));
    }
}
