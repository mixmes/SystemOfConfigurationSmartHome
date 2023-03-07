package ru.sfedu.services;

import ru.sfedu.model.Notification;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Notification.class)
public class Wrapper<T> {
    @XmlElement
    @XmlElementWrapper
    private List<T> beans = new ArrayList<>();

    public Wrapper() {
    }

    public Wrapper(List<T> beans) {
        this.beans = beans;
    }

    public List<T> getBeans() {
        return beans;
    }

    public void setBeans(List<T> beans) {
        this.beans = beans;
    }
}
