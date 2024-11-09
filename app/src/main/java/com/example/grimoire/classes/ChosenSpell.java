package com.example.grimoire.classes;

import java.io.Serializable;

public class ChosenSpell implements Serializable {
    private int id;
    private int spellId;
    private int characterId;


    public ChosenSpell(int id, int spellId, int characterId) {
        this.id = id;
        this.spellId = spellId;
        this.characterId = characterId;
    }

    public ChosenSpell(int spellId, int characterId) {
        this.spellId = spellId;
        this.characterId = characterId;
    }

    public ChosenSpell() {}


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
}
