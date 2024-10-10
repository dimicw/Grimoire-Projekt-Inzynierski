package com.example.grimuare;

import java.io.Serializable;

public class BoundSpell implements Serializable {
    private int spellId;
    private int spellImage;

    public BoundSpell(int spellId, int spellImage) {
        this.spellId = spellId;
        this.spellImage = spellImage;
    }

    public int getSpellId() {
        return spellId;
    }
    public int getSpellImage() {
        return spellImage;
    }
    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }
    public void setSpellImage(int spellImage) {
        this.spellImage = spellImage;
    }
}
