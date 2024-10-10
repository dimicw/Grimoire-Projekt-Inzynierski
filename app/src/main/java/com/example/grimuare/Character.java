package com.example.grimuare;

import java.io.Serializable;
import java.util.ArrayList;

public class Character implements Serializable {
    private String name;
    private int strength, dexterity, constitution, intelligence, wisdom, charisma;
    private int level;
    private String mainClass;
    private int image;
    private ArrayList<BoundSpell> boundSpells;

    public Character(String name, int strength, int dexterity, int constitution,
                     int intelligence, int wisdom, int charisma,
                     int level, String mainClass, int image) {
        this.name = name;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.level = level;
        this.mainClass = mainClass;
        this.image = image;
        this.boundSpells = new ArrayList<>();
    }

    public Character(String name, String mainClass) {
        this.name = name;
        this.strength = 10;
        this.dexterity = 10;
        this.constitution = 10;
        this.intelligence = 10;
        this.wisdom = 10;
        this.charisma = 10;
        this.level = 1;
        this.mainClass = mainClass;

        switch(mainClass){
            case "Artificer":
                this.image = R.drawable.class_icon___artificer;
                break;
            case "Bard":
                this.image = R.drawable.class_icon___bard;
                break;
            case "Cleric":
                this.image = R.drawable.class_icon___cleric;
                break;
            case "Druid":
                this.image = R.drawable.class_icon___druid;
                break;
            case "Paladin":
                this.image = R.drawable.class_icon___paladin;
                break;
            case "Ranger":
                this.image = R.drawable.class_icon___ranger;
                break;
            case "Sorcerer":
                this.image = R.drawable.class_icon___sorcerer;
                break;
            case "Warlock":
                this.image = R.drawable.class_icon___warlock;
                break;
            case "Wizard":
                this.image = R.drawable.class_icon___wizard;
                break;
            default:
                this.image = R.drawable.big_book;
                break;
        }

        this.boundSpells = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public int getStrength() {
        return strength;
    }
    public int getDexterity() {
        return dexterity;
    }
    public int getConstitution() {
        return constitution;
    }
    public int getIntelligence() {
        return intelligence;
    }
    public int getWisdom() {
        return wisdom;
    }
    public int getCharisma() {
        return charisma;
    }
    public int getLevel() {
        return level;
    }
    public int getImage() {
        return image;
    }
    public String getMainClass() {
        return mainClass;
    }
    public ArrayList<BoundSpell> getBoundSpells() {
        return boundSpells;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }
    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }
    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
    public void setBoundSpells(ArrayList<BoundSpell> boundSpells) {
        this.boundSpells = boundSpells;
    }

    public int getModifier(String ability) {
        int modifier = 0;
        switch(ability) {
            case "str":
                modifier = getAbilityModifier(this.strength);
                break;
            case "dex":
                modifier = getAbilityModifier(this.dexterity);
                break;
            case "con":
                modifier = getAbilityModifier(this.constitution);
                break;
            case "int":
                modifier = getAbilityModifier(this.intelligence);
                break;
            case "wis":
                modifier = getAbilityModifier(this.wisdom);
                break;
            case "cha":
                modifier = getAbilityModifier(this.charisma);
                break;
        }
        modifier += 2 + (this.level - 1) / 4;

        return modifier;
    }

    public void addSpell(int spellId, int spellImage) {
        this.boundSpells.add(new BoundSpell(spellId, spellImage));
    }

    public void removeSpellById(int spellId) {
        this.boundSpells.removeIf(boundSpell -> boundSpell.getSpellId() == spellId);
    }

    private int getAbilityModifier(int value) {
        return value / 2 - 5;
    }
}
