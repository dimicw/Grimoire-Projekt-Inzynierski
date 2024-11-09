package com.example.grimoire.classes;

import java.io.Serializable;

public class Character implements Serializable {
    private int id;
    private String name;
    private int classId;

    public Character() {}

    public Character(int id, String name, int classId) {
        this.id = id;
        this.name = name;
        this.classId = classId;
    }

    public Character(String name, int classId) {
        this.name = name;
        this.classId = classId;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getClassId() {
        return classId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }
}
