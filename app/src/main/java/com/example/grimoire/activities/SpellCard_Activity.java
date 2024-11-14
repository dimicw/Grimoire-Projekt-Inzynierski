package com.example.grimoire.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.SpellModel;

public class SpellCard_Activity extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;

    private DatabaseHelper dbHelper;

    private ImageButton backButton;
    private Toolbar toolbar;
    private ImageView backgroundImage;
    private TextView tvName, tvLevelAndSchool, tvCastingTime, tvRange, tvComponents, tvDuration, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_card);

        SpellModel spellModel;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

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
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}