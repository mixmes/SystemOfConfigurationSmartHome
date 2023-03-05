package ru.sfedu.model;

import java.util.List;

public class SmartHome  implements EntityBean  {
    private long id;
    private String name;
    private List<Device> devices;

    public SmartHome() {
    }

    public SmartHome(long id, String name, List<Device> devices) {
        this.id = id;
        this.name = name;
        this.devices = devices;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public long getID() {
        return this.id;
    }
}
