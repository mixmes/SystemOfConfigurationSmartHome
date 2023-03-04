package ru.sfedu.model;

import java.util.List;

public class SmartHome {
    private long id;
    private String name;
    private List<User> residents;
    private List<Device> devices;

    public SmartHome() {
    }

    public SmartHome(long id, String name, List<User> residents, List<Device> devices) {
        this.id = id;
        this.name = name;
        this.residents = residents;
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

    public List<User> getResidents() {
        return residents;
    }

    public void setResidents(List<User> residents) {
        this.residents = residents;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
