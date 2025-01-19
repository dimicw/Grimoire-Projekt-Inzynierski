package com.example.grimoire.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.grimoire.R;

public class LicenseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);


        TextView wotc_license = findViewById(R.id.wotc_license);
        TextView cc3_license = findViewById(R.id.cc3_license);

        wotc_license.setText(HtmlCompat.fromHtml(getString(R.string.wotc_license),
                HtmlCompat.FROM_HTML_MODE_LEGACY));
        wotc_license.setMovementMethod(LinkMovementMethod.getInstance());

        cc3_license.setText(HtmlCompat.fromHtml(getString(R.string.cc3_license),
                HtmlCompat.FROM_HTML_MODE_LEGACY));
        cc3_license.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
