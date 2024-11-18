package com.example.grimoire.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.SpellModel;

public class SpellCard_Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_card);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Intent intent;
        Bundle bundle;

        Toolbar toolbar;
        ImageView backgroundImage;
        TextView tvName, tvLevelAndSchool, tvCastingTime, tvRange, tvComponents, tvDuration, tvDescription;

        SpellModel spellModel;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        try {
            intent = getIntent();
            bundle = intent.getExtras();

            backgroundImage = findViewById(R.id.backgroundImage);
            tvName = findViewById(R.id.nameCard);
            tvLevelAndSchool = findViewById(R.id.levelAndSchoolCard);
            tvCastingTime = findViewById(R.id.castingTimeCard);
            tvRange = findViewById(R.id.rangeCard);
            tvComponents = findViewById(R.id.componentsCard);
            tvDuration = findViewById(R.id.durationCard);
            tvDescription = findViewById(R.id.descriptionCard);

            if (bundle != null) {

                spellModel = (SpellModel) bundle.getSerializable("SPELL");
                int classImage = bundle.getInt("CLASS_IMAGE");

                if (spellModel != null) {
                    String name = spellModel.getName();

                    getSupportActionBar().setTitle(name);

                    tvDescription.setMovementMethod(new ScrollingMovementMethod());
                    backgroundImage.setImageResource(classImage);

                    tvName.setText(name);
                    tvLevelAndSchool.setText(spellModel.getLevelAndSchool(dbHelper));
                    tvCastingTime.append(" " + spellModel.getCastingTime());
                    tvRange.append(" " + spellModel.getRange());
                    tvDuration.append(" " + spellModel.getDuration());
                    tvDescription.append(spellModel.getDescription());

                    String atHigherLevels = spellModel.getAtHigherLevels();
                    if (atHigherLevels != null) {
                        String higherLevelsLabel = (spellModel.getLevel() == 0) ? "\n\n" : "\n\nAt Higher Levels: ";
                        tvDescription.append(higherLevelsLabel + atHigherLevels);
                    }

                    if (spellModel.isV()) {
                        tvComponents.append(" V");
                        if (spellModel.isS() || spellModel.isM())
                            tvComponents.append(",");
                    }
                    if (spellModel.isS()) {
                        tvComponents.append(" S");
                        if (spellModel.isM())
                            tvComponents.append(",");
                    }
                    if (spellModel.isM())
                        tvComponents.append(" M: " + spellModel.getComponents());
                } else {
                    Toast.makeText(this, "spell is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "error bundle", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("DatabaseHelper", "Error creating Directory ", e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}