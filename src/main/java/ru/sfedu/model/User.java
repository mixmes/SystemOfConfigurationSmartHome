package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@XmlAccessorType(XmlAccessType.FIELD)
public class User implements EntityBean  {
    @XmlElement(name = "name")
    private String name ;
    @XmlElement(name = "id")
    private long id;
    @XmlTransient
    private SmartHome smartHome;
    private long smartHomeId;
    @XmlElement(name = "accessLevel")
    private Constants.AccessLevel accessLevel= Constants.AccessLevel.ADMIN;
    @XmlTransient
    private static final Logger log =  LogManager.getLogger(User.class);

    public User() {
    }

    public User(long id, String name, Constants.AccessLevel level) {
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


    public void setId(long id) {
        this.id = id;
    }

    public Constants.AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Constants.AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public SmartHome getSmartHome() {
        return smartHome;
    }

    public long getSmartHomeId() {
        return smartHomeId;
    }

    public void setSmartHomeId(long smartHomeId) {
        this.smartHomeId = smartHomeId;
    }

    public void setSmartHome(SmartHome smartHome) {
        this.smartHome = smartHome;
        smartHomeId = smartHome.getId();
    }

    @Override
    public long getId() {
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
    public void deleteDeviceInSmartHome(Device device) throws Exception {
        checkDeviceInSmartHome(device);
        smartHome.getDevices().remove(getDeviceFromSmartHome(device));
    }
    public void addResidentToSmartHome(User user) throws Exception {
        checkAccessLevel();
        user.setSmartHome(smartHome);
        user.setAccessLevel(Constants.AccessLevel.RESIDENT);
    }
    public void deleteResidentInSmartHome(User user) throws Exception {
        checkAccessLevel();
        user.setSmartHome(null);
        user.setAccessLevel(Constants.AccessLevel.ADMIN);
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
        boolean actualState = ((Lock)getDeviceFromSmartHome(lock)).isState();
        generateNotificationAboutChangedState(lock,actualState);

    }
    public void changeStateHeater(Heater h) throws Exception {
        Device heater = checkDeviceInSmartHome(h);
        boolean changesState = !((Heater)getDeviceFromSmartHome(heater)).isState();
        ((Heater)getDeviceFromSmartHome(heater)).setState(changesState);
        log.info("User "+name+" changes heater state "+h.getName()+" to "+((Heater)getDeviceFromSmartHome(heater)).isState());
        boolean actualState = ((Heater)getDeviceFromSmartHome(heater)).isState();
        generateNotificationAboutChangedState(heater,actualState);
    }
    public void changeHeatersPower(Heater h, int power) throws Exception {
        Device heater = checkDeviceInSmartHome(h);
        if (h.getMaxPower() >= power) {
            ((Heater) getDeviceFromSmartHome(heater)).setCurrentPower(power);
            generateNotificationAboutChangedPower(heater,power);
        } else {
            log.error("Max power+"+h.getMaxPower()+" < transmited power");
            throw new Exception("Power is higher than allowed");
        }
    }

    public void automateWorkHeater(Heater h,int tempOn, int tempOff) throws Exception {
        Device heater = checkDeviceInSmartHome(h);
        ((Heater)getDeviceFromSmartHome(heater)).setTemperatureForOn(tempOn);
        ((Heater)getDeviceFromSmartHome(heater)).setTemperatureForOff(tempOff);
    }
    public void changeStateSocket(Socket s) throws Exception {
        Device socket=checkDeviceInSmartHome(s);
        boolean changesState = !((Socket)getDeviceFromSmartHome(socket)).isState();
        ((Socket)getDeviceFromSmartHome(socket)).setState(changesState);
        boolean actualState = ((Socket)getDeviceFromSmartHome(socket)).isState();
        generateNotificationAboutChangedState(socket,actualState);
    }
    public void changeStateHumidifier(Humidifier h) throws Exception {
        Device humidifier = checkDeviceInSmartHome(h);
        boolean changesState = !((Humidifier)getDeviceFromSmartHome(humidifier)).isState();
        ((Humidifier)getDeviceFromSmartHome(humidifier)).setState(changesState);
        boolean actualState = ((Humidifier)getDeviceFromSmartHome(humidifier)).isState();
        generateNotificationAboutChangedState(humidifier,actualState);
    }
    public void changeHumidifierPower(Humidifier h,int power) throws Exception {
        Device humidifier = checkDeviceInSmartHome(h);
        if(((Humidifier)humidifier).getMaxPower()>=power){
            ((Humidifier)getDeviceFromSmartHome(humidifier)).setCurrentPower(power);
            generateNotificationAboutChangedPower(humidifier,power);
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
        boolean actualState = ((Lamp)getDeviceFromSmartHome(lamp)).isState();
        generateNotificationAboutChangedState(lamp,actualState);
    }

    public void changeLampBrightness(Lamp l,int brightness) throws Exception {
        Device lamp = checkDeviceInSmartHome(l);
        if(l.getMaxBrightness()>=brightness){
            ((Lamp)getDeviceFromSmartHome(lamp)).setCurrentBrightness(brightness);
            generateNotificationAboutChangedPower(lamp,brightness);
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
    private void generateNotificationAboutChangedState(Device device, boolean actualState) throws ParseException {
        getDeviceFromSmartHome(device).generateNotification("Changed its state to "+(actualState?"on":"off"));
    }
    private void generateNotificationAboutChangedPower(Device device, int power) throws ParseException {
        getDeviceFromSmartHome(device).generateNotification("Changed its power to "+power);
    }
    private void checkAccessLevel() throws Exception {
        if(accessLevel!= Constants.AccessLevel.ADMIN){
            log.error("The user does not have enough rights to add user");
            throw new Exception("Insufficient rights to add residents");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(smartHome, user.smartHome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, smartHome);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", smartHome=" + smartHome +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
