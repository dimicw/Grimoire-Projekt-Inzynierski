package com.example.grimoire.interfaces;

import com.example.grimoire.models.SpellModel;

public interface SpellClickListener {
    void onAddSpellClick(SpellModel spellModel);
    void onOpenSpellCardClick(int casterClassId, SpellModel spellModel);
    void onDeleteSpellClick(SpellModel spellModel);
}
