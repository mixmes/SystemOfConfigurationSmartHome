package ru.sfedu.model;

import java.util.List;

public class Socket extends Device {
    private boolean state=false;

    public Socket() {
    }

    public Socket(long id, String name) {
        super(id, name);
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
