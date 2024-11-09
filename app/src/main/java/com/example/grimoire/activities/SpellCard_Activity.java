package com.example.grimoire.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    int spellId;
    int currentCharacterId;
    Spell spell;

    ImageButton backButton;
    ImageView backgroundImage;
    TextView tvName, tvLevelAndSchool, tvCastingTime, tvRange, tvComponents, tvDuration, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_card);

        dbHelper = new DatabaseHelper(this);

        try {
            intent = getIntent();
            bundle = intent.getExtras();

            backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(view -> onBackPressed());

            if (bundle != null) {
                spellId = bundle.getInt("SPELL_ID");
                currentCharacterId = bundle.getInt("CHARACTER_ID");

                spell = dbHelper.getSpellById(spellId);
                backgroundImage = findViewById(R.id.backgroundImage);

                if (currentCharacterId > 0) {
                    int casterClassId = dbHelper.getCharacterById(currentCharacterId).getClassId();
                    CasterClass casterClass = dbHelper.getClassById(casterClassId);

                    backgroundImage.setImageResource(casterClass.getClassImage());
                } else {

                    backgroundImage.setImageResource(R.drawable.big_book);
                }

                if (spell != null) {
                    tvName = findViewById(R.id.nameCard);
                    tvLevelAndSchool = findViewById(R.id.levelAndSchoolCard);
                    tvCastingTime = findViewById(R.id.castingTimeCard);
                    tvRange = findViewById(R.id.rangeCard);
                    tvComponents = findViewById(R.id.componentsCard);
                    tvDuration = findViewById(R.id.durationCard);
                    tvDescription = findViewById(R.id.descriptionCard);

                    tvDescription.setMovementMethod(new ScrollingMovementMethod());

                    tvName.setText(spell.getName());
                    tvLevelAndSchool.setText(spell.getLevelAndSchool());
                    tvCastingTime.append(" " + spell.getCastingTime());
                    tvRange.append(" " + spell.getRange());
                    tvDuration.append(" " + spell.getDuration());
                    tvDescription.append(spell.getDescription());

                    if (spell.isV()) {
                        tvComponents.append(" V");
                        if (spell.isS() || spell.isM())
                            tvComponents.append(",");
                    }
                    if (spell.isS()) {
                        tvComponents.append(" S");
                        if (spell.isM())
                            tvComponents.append(",");
                    }
                    if (spell.isM())
                        tvComponents.append(" M: " + spell.getComponents());
                } else {
                    Toast.makeText(this, "spell is null", Toast.LENGTH_SHORT).show();
                    //System.out.println(dbHelper.getAllSpells().toString());
                    System.out.println("" + spellId);
                }
            } else {
                Toast.makeText(this, "error bundle", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}