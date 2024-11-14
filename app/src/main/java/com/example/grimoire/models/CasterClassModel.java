package com.example.grimoire.models;

import androidx.annotation.NonNull;

import com.example.grimoire.R;

import java.io.Serializable;

public class CasterClassModel implements Serializable {
    private int id;
    private String name;

    public CasterClassModel() {}

    public CasterClassModel(String name) {
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


    public int getClassImage () {
        switch (name.toLowerCase()) {
            case "bard":
                return R.drawable.class_icon___bard;
            case "cleric":
                return R.drawable.class_icon___cleric;
            case "druid":
                return R.drawable.class_icon___druid;
            case "paladin":
                return R.drawable.class_icon___paladin;
            case "ranger":
                return R.drawable.class_icon___ranger;
            case "sorcerer":
                return R.drawable.class_icon___sorcerer;
            case "warlock":
                return R.drawable.class_icon___warlock;
            case "wizard":
                return R.drawable.class_icon___wizard;
            default:
                return R.drawable.spell_book;
        }
    }


    @NonNull
    @Override
    public String toString() {
        return "CasterClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
