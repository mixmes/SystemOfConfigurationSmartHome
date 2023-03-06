package ru.sfedu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class User implements EntityBean  {
    private String name ;
    private long id;
    private SmartHome smartHome;
    private Constants.AcessLevel accessLevel;
    private Logger log =  LogManager.getLogger(User.class);

    public User() {
    }

    public User(long id, String name) {
        this.name = name;
        this.id = id;
        this.accessLevel = Constants.AcessLevel.ADMIN;
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
        if(accessLevel.equals(Constants.AcessLevel.ADMIN)){
            user.setSmartHome(smartHome);
            user.setAccessLevel(Constants.AcessLevel.RESIDENT);
        }else

            throw new Exception("Insufficient rights to add residents");
    }
    public List<Notification> checkSmartHomesNotification(){
        List<Notification> notifications = new ArrayList<>();
        smartHome.getDevices().forEach(d->notifications.addAll(d.getNotifications()));
        return notifications;
    }

    public void changeStateLock(Lock l){
        Optional<Device> lock =  smartHome.getDevices().stream().filter(d->d.equals(l)).findFirst();
        int index = smartHome.getDevices().indexOf(lock);
        boolean state = ((Lock)smartHome.getDevices().get(index)).isState();
        if (lock.isPresent()){
            ((Lock)smartHome.getDevices().get(index)).setState(!state);
            log.info("User "+name+" changes lock state "+l.getName()+" to "+((Lock)smartHome.getDevices().get(index)).isState());
        }

    }
}
