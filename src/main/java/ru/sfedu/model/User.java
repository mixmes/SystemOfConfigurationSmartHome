package ru.sfedu.model;

public class User implements EntityBean  {
    private String name ;
    private long id;
    private String AccessLevel;

    public User() {
    }

    public User(String name, long id, String accessLevel) {
        this.name = name;
        this.id = id;
        AccessLevel = accessLevel;
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

    public String getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        AccessLevel = accessLevel;
    }

    @Override
    public long getID() {
        return this.id;
    }
}
