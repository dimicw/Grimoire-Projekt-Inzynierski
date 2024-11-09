package com.example.grimoire.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.classes.CasterClass;
import com.example.grimoire.classes.ChosenSpell;
import com.example.grimoire.classes.Spell;
import com.example.grimoire.classes.Character;

public class SpellCard_Activity extends AppCompatActivity {

    Intent intent;
    Bundle bundle;

    DatabaseHelper dbHelper;

    ChosenSpell chosenSpell;
    Spell spell;

    ImageButton backButton;
    ImageView backgroundImage;
    TextView tvName, tvLevelAndSchool, tvCastingTime, tvRange, tvComponents, tvDuration, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_card);

        dbHelper = new DatabaseHelper(this);

        intent = getIntent();
        bundle = intent.getExtras();

        chosenSpell = (ChosenSpell) bundle.getSerializable("SPELL");
        Spell spell = dbHelper.getSpellById(chosenSpell.getSpellId());
        Character character = dbHelper.getCharacterById(chosenSpell.getCharacterId());
        CasterClass casterClass = dbHelper.getClassById(character.getClassId());

        backButton = findViewById(R.id.backButton);
        backgroundImage = findViewById(R.id.backgroundImage);
        tvName = findViewById(R.id.nameCard);
        tvLevelAndSchool = findViewById(R.id.levelAndSchoolCard);
        tvCastingTime = findViewById(R.id.castingTimeCard);
        tvRange = findViewById(R.id.rangeCard);
        tvComponents = findViewById(R.id.componentsCard);
        tvDuration = findViewById(R.id.durationCard);
        tvDescription = findViewById(R.id.descriptionCard);

        tvDescription.setMovementMethod(new ScrollingMovementMethod());

        backButton.setOnClickListener(view -> onBackPressed());

        backgroundImage.setImageResource(casterClass.getClassImage());
        tvName.setText(spell.getName());
        tvLevelAndSchool.setText(spell.getLevelAndSchool());
        tvCastingTime.append(" " + spell.getCastingTime());
        tvRange.append(" " + spell.getRange());
        tvDuration.append(" " + spell.getDuration());
        /*for (int i = 0; i < spell.getDescription().length; i++)
            tvDescription.setText(spell.getDescription()[i] + "\n\n");*/
        tvDescription.append(spell.getDescription());

        if(spell.isV()) {
            tvComponents.append(" V");
            if (spell.isS() || spell.isM())
                tvComponents.append(",");
        }
        if(spell.isS()){
            tvComponents.append(" S");
            if (spell.isM())
                tvComponents.append(",");
        }
        if (spell.isM())
            tvComponents.append(" M: " + spell.getComponents());
    }
}