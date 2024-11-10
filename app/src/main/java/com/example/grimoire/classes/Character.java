package com.example.grimoire.classes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Character implements Serializable {
    private int id;
    private String name;
    private int classId;

    public Character() {}

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


    @NonNull
    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classId=" + classId +
                '}';
    }
}
