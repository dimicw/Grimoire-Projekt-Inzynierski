package com.example.grimoire.dialogs;

import android.app.AlertDialog;
import android.content.Context;

public class DeleteConfirmDialog {

    private final String question;
    private final String title;
    private final Context context;
    private final OnDeleteConfirmListener listener;

    public DeleteConfirmDialog(Context context, String name, boolean isSpell,
                               OnDeleteConfirmListener listener) {
        this.context = context;
        this.title = "Delete " + ((isSpell) ? "Spell" : "Character");
        this.question = "Are you sure you want to delete the " +
                ((isSpell) ? "spell \"" + name + "\" from your chosen spells?"
                : "character \"" + name + "\"?");
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(question)
                .setPositiveButton("Yes", (dialog, which) -> listener.onConfirm(true))
                .setNegativeButton("No", (dialog, which) -> listener.onConfirm(false));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnDeleteConfirmListener {
        void onConfirm(boolean confirmed);
    }
}
