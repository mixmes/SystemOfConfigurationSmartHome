package ru.sfedu.model;

import java.util.ArrayList;
import java.util.List;

public class Device {
    private long id;
    private Sensor sensor;
    private String name;
    private List<Notification> notifications = new ArrayList<>();

}
