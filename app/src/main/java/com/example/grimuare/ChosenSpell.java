package com.example.grimuare;

public class ChosenSpell extends Spell{
    private int image;

    public ChosenSpell(Spell spell, int image) {
        super(spell.getName(), spell.getSource(), spell.getLevel(), spell.getSchool(),
                spell.getCastingTime(), spell.isRitual(), spell.getRange(), spell.getComponents(),
                spell.isV(), spell.isS(), spell.isM(), spell.getDuration(), spell.isConcentration(),
                spell.getDescription());
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
