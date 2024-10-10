package com.example.grimuare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SpellCard_Activity extends AppCompatActivity {

    Intent intent;
    Bundle bundle;

    ChosenSpell spell;

    ImageButton backButton;
    ImageView backgroundImage;
    TextView tvName, tvLevelAndSchool, tvCastingTime, tvRange, tvComponents, tvDuration, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_card);

        intent = getIntent();
        bundle = intent.getExtras();

        spell = (ChosenSpell)bundle.getSerializable("SPELL");

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

        backgroundImage.setImageResource(spell.getImage());
        tvName.setText(spell.getName());
        tvLevelAndSchool.setText(spell.getLevelAndSchool());
        tvCastingTime.append(" " + spell.getCastingTime());
        tvRange.append(" " + spell.getRange());
        tvDuration.append(" " + spell.getDuration());
        for (int i = 0; i < spell.getDescription().length; i++)
            tvDescription.setText(spell.getDescription()[i] + "\n\n");

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