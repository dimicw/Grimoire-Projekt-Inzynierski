package com.example.grimoire.models;

import androidx.annotation.NonNull;

import com.example.grimoire.Helpers.DatabaseHelper;

import java.io.Serializable;

public class SpellModel implements Serializable {
    private int id;
    private String name;
    private int level;
    private int schoolId;
    private String castingTime;
    private boolean ritual;
    private String range;
    private String components;
    private boolean v, s, m;
    private String duration;
    private boolean concentration;
    private String description;

    public SpellModel(String name, int level, int schoolId, String castingTime,
                      boolean ritual, String range, String components, boolean v, boolean s, boolean m,
                      String duration, boolean concentration, String description) {
        this.name = name;
        this.level = level;
        this.schoolId = schoolId;
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

    public SpellModel() {

    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
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
    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getLevel() {
        return level;
    }
    public int getSchoolId() {
        return schoolId;
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
    public String getDescription() {
        return description;
    }

    public String getLevelAndSchool(DatabaseHelper dbHelper) {
        String levelAndSchool, schoolName;
        SchoolModel schoolModel = dbHelper.getSchoolById(schoolId);
        schoolName = (schoolModel != null) ? schoolModel.getName() : "school";

        if (level == 0)
            return schoolName + " cantrip";

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

        levelAndSchool += "-level " + schoolName;

        return levelAndSchool;
    }

    @NonNull
    @Override
    public String toString() {
        return "Spell{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", schoolId=" + schoolId +
                ", castingTime='" + castingTime + '\'' +
                ", ritual=" + ritual +
                ", range='" + range + '\'' +
                ", components='" + components + '\'' +
                ", v=" + v +
                ", s=" + s +
                ", m=" + m +
                ", duration='" + duration + '\'' +
                ", concentration=" + concentration +
                ", description='" + description + '\'' +
                '}';
    }
}