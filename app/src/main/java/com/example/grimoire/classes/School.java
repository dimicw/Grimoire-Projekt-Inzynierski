package com.example.grimoire.classes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class School implements Serializable {
    private int id;
    private String name;

    public School () {}

    public School (String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }


    @NonNull
    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
