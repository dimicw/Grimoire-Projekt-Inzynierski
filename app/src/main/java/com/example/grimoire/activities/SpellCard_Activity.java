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
import com.example.grimoire.classes.Spell;

public class SpellCard_Activity extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;

    private DatabaseHelper dbHelper;

    private ImageButton backButton;
    private ImageView backgroundImage;
    private TextView tvName, tvLevelAndSchool, tvCastingTime, tvRange, tvComponents, tvDuration, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_card);

        int spellId;
        int currentCharacterId;
        Spell spell;

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