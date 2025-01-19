package com.example.grimoire.dialogs;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.grimoire.R;
import com.example.grimoire.activities.LicenseActivity;

public class HelpDialog {

    Context context;

    private AlertDialog dialog;


    public HelpDialog(Context context) {
        this.context = context;
    }

    public void show() {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_help, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.license_information);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LicenseActivity.class);
            context.startActivity(intent);
            dialog.dismiss();
        });


        Button buttonOk = dialogView.findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(v -> dialog.dismiss());

        dialog = builder.create();
        dialog.show();
    }

}
