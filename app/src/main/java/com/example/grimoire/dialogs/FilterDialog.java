package com.example.grimoire.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.SchoolModel;
import com.example.grimoire.adapters.SpellRecyclerViewAdapter;

import java.util.ArrayList;

public class FilterDialog {

    private final Context context;
    private final DatabaseHelper dbHelper;
    private final SpellRecyclerViewAdapter adapter;
    private final SearchView searchView;
    private AlertDialog dialog;

    ArrayList<SchoolModel> schoolModels;

    RadioGroup radioGroupRitual;
    RadioGroup radioGroupConcentration;
    RadioGroup radioGroupV;
    RadioGroup radioGroupS;
    RadioGroup radioGroupM;

    LinearLayout schoolCheckboxContainer;

    CheckBox[] checkBoxes;

    public FilterDialog(Context context, DatabaseHelper dbHelper, SpellRecyclerViewAdapter adapter, SearchView searchView) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.adapter = adapter;
        this.searchView = searchView;
    }

    public void show() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filter, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        radioGroupRitual = dialogView.findViewById(R.id.radioGroupRitual);
        radioGroupConcentration = dialogView.findViewById(R.id.radioGroupConcentration);
        radioGroupV = dialogView.findViewById(R.id.radioGroupV);
        radioGroupS = dialogView.findViewById(R.id.radioGroupS);
        radioGroupM = dialogView.findViewById(R.id.radioGroupM);

        schoolCheckboxContainer = dialogView.findViewById(R.id.schoolCheckboxContainer);

        checkBoxes = new CheckBox[10];
        for (int i = 0; i <= 9; i++) {
            @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier("checkBoxLevel" + i, "id", context.getPackageName());
            checkBoxes[i] = dialogView.findViewById(resId);
        }

        schoolModels = dbHelper.getAllSchools();
        for (SchoolModel school : schoolModels)
            generateCheckbox(school);

        Button buttonApplyFilter = dialogView.findViewById(R.id.buttonApplyFilter);

        buttonApplyFilter.setOnClickListener(v -> onClickListener());

        dialog = builder.create();
        dialog.show();
    }

    private void generateCheckbox(SchoolModel schoolModel) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText(schoolModel.getName());
        checkBox.setChecked(true);
        checkBox.setPadding(16, 16, 16, 16);
        params.setMargins(0, 8, 0, 8);
        checkBox.setLayoutParams(params);

        int primaryColor = ContextCompat.getColor(context, R.color.colorAccent);
        checkBox.setButtonTintList(ColorStateList.valueOf(primaryColor));

        schoolCheckboxContainer.addView(checkBox);
    }

    private void onClickListener() {
        int ritualFilter = radioGroupRitual.getCheckedRadioButtonId();
        int concentrationFilter = radioGroupConcentration.getCheckedRadioButtonId();
        int VFilter = radioGroupV.getCheckedRadioButtonId();
        int sFilter = radioGroupS.getCheckedRadioButtonId();
        int mFilter = radioGroupM.getCheckedRadioButtonId();
        boolean[] levelFilters = new boolean[10];
        for (int i = 0; i <= 9; i++)
            levelFilters[i] = checkBoxes[i].isChecked();

        int[] selectedSchools = new int[schoolModels.size()];
        for (int i = 0; i < schoolCheckboxContainer.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) schoolCheckboxContainer.getChildAt(i);
            if (checkBox.isChecked()) {
                String schoolName = checkBox.getText().toString();
                selectedSchools[i] = dbHelper.getSchoolByName(schoolName).getId();
            }
        }

        adapter.applyFilters(ritualFilter, concentrationFilter, levelFilters,
                VFilter, sFilter, mFilter, selectedSchools);
        adapter.filter(searchView.getQuery().toString());

        dialog.dismiss();
    }
}

