package ru.sfedu.services;

import ru.sfedu.model.Notification;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Notification.class)
public class Wrapper<T> {
    @XmlElement
    @XmlElementWrapper
    private ArrayList<T> beans = new ArrayList<>();

    public Wrapper() {
    }

    public Wrapper(ArrayList<T> beans) {
        this.beans = beans;
    }

    public ArrayList<T> getBeans() {
        return beans;
    }

    public void setBeans(ArrayList<T> beans) {
        this.beans = beans;
    }
}
