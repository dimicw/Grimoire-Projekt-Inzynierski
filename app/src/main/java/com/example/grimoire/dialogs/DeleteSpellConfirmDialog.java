package com.example.grimoire.dialogs;

import android.app.AlertDialog;
import android.content.Context;

public class DeleteSpellConfirmDialog {

    private final String spellName;
    private final Context context;
    private final OnDeleteConfirmListener listener;

    public DeleteSpellConfirmDialog(Context context, String spellName, OnDeleteConfirmListener listener) {
        this.context = context;
        this.spellName = spellName;
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Spell")
                .setMessage("Are you sure you want to delete the spell \"" + spellName + "\"?")
                .setPositiveButton("Yes", (dialog, which) -> listener.onConfirm(true))
                .setNegativeButton("No", (dialog, which) -> listener.onConfirm(false));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnDeleteConfirmListener {
        void onConfirm(boolean confirmed);
    }
}
