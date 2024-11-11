package com.example.grimoire.models;

import java.io.Serializable;

public class ChosenSpellModel implements Serializable {
    private int id;
    private int spellId;
    private int characterId;

    public ChosenSpellModel() {}

    public ChosenSpellModel(int spellId, int characterId) {
        this.spellId = spellId;
        this.characterId = characterId;
    }


    public int getId() {
        return id;
    }
    public int getSpellId() {
        return spellId;
    }
    public int getCharacterId() {
        return characterId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }


    @Override
    public String toString() {
        return "ChosenSpell{" +
                "id=" + id +
                ", spellId=" + spellId +
                ", characterId=" + characterId +
                '}';
    }
}
