package com.example.grimuare;

import java.io.Serializable;

public class Spell implements Serializable {
    private String name;
    private String source;
    private int level;
    private String school;
    private String castingTime;
    private boolean ritual;
    private String range;
    private String components;
    private boolean v, s, m;
    private String duration;
    private boolean concentration;
    private String[] description;

    public Spell(String name, String source, int level, String school, String castingTime,
                 boolean ritual, String range, String components, boolean v, boolean s, boolean m,
                 String duration, boolean concentration, String[] description) {
        this.name = name;
        this.source = source;
        this.level = level;
        this.school = school;
        this.castingTime = castingTime;
        this.ritual = ritual;
        this.range = range;
        this.components = components;
        this.v = v;
        this.s = s;
        this.m = m;
        this.duration = duration;
        this.concentration = concentration;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public void setCastingTime(String castingTime) {
        this.castingTime = castingTime;
    }
    public void setRitual(boolean ritual) {
        this.ritual = ritual;
    }
    public void setRange(String range) {
        this.range = range;
    }
    public void setComponents(String components) {
        this.components = components;
    }
    public void setV(boolean v) {
        this.v = v;
    }
    public void setS(boolean s) {
        this.s = s;
    }
    public void setM(boolean m) {
        this.m = m;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setConcentration(boolean concentration) {
        this.concentration = concentration;
    }
    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public int getLevel() {
        return level;
    }
    public String getSchool() {
        return school;
    }
    public String getCastingTime() {
        return castingTime;
    }
    public boolean isRitual() {
        return ritual;
    }
    public String getRange() {
        return range;
    }
    public String getComponents() {
        return components;
    }
    public boolean isV() {
        return v;
    }
    public boolean isS() {
        return s;
    }
    public boolean isM() {
        return m;
    }
    public String getDuration() {
        return duration;
    }
    public boolean isConcentration() {
        return concentration;
    }
    public String[] getDescription() {
        return description;
    }
    public String getLevelAndSchool() {
        String levelAndSchool;

        if (level == 0)
            return school + " cantrip";

        switch(level) {
            case 1:
                levelAndSchool = "1st";
                break;
            case 2:
                levelAndSchool = "2nd";
                break;
            case 3:
                levelAndSchool = "3rd";
                break;
            default:
                levelAndSchool = level + "th";
                break;
        }

        levelAndSchool += "-level " + school;

        return levelAndSchool;
    }
}