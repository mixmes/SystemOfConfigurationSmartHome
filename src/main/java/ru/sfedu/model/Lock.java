package ru.sfedu.model;

import java.util.List;

public class Lock extends Device{
    private boolean state=true;

    public Lock() {
    }

    public Lock(long id, String name) {
        super(id, name);

    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
