package ru.sfedu.services;

import java.util.ArrayList;

public class Wrapper<T> {
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
