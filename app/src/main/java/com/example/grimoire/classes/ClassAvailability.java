package com.example.grimoire.classes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ClassAvailability implements Serializable {
    private int id;
    private int spellId;
    private int classId;

    public ClassAvailability() {}

    public ClassAvailability(int spellId, int classId) {
        this.spellId = spellId;
        this.classId = classId;
    }


    public int getId() {
        return id;
    }
    public int getSpellId() {
        return spellId;
    }
    public int getClassId() {
        return classId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassAvailability{" +
                "id=" + id +
                ", spellId=" + spellId +
                ", classId=" + classId +
                '}';
    }
}
