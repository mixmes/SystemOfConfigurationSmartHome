package ru.sfedu.model;

import ru.sfedu.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User implements EntityBean  {
    private String name ;
    private long id;
    private SmartHome smartHome;
    private Constants.AcessLevel accessLevel;

    public User() {
    }

    public User(String name, long id) {
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

    public void addResidentToSmartHome(User user){
        if(accessLevel.equals(Constants.AcessLevel.ADMIN)){
            user.setSmartHome(smartHome);
            user.setAccessLevel(Constants.AcessLevel.RESIDENT);
        }
    }
    public List<Notification> checkSmartHomesNotification(){
        List<Notification> notifications = new ArrayList<>();
        smartHome.getDevices().forEach(d->notifications.addAll(d.getNotifications()));
        return notifications;
    }

    public void openLock(Lock lock){

    }
}
