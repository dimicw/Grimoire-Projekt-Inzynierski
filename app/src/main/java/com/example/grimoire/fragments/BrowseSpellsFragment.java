package com.example.grimoire.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.SchoolModel;
import com.example.grimoire.models.SpellModel;
import com.example.grimoire.interfaces.RecyclerViewInterface;
import com.example.grimoire.activities.SpellCard_Activity;
import com.example.grimoire.adapters.Spell_RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class BrowseSpellsFragment extends Fragment implements RecyclerViewInterface {
    public interface SpellClickListener {
        void onSpellLongClick(int position);
    }

    private SpellClickListener spellClickListener;

    private DatabaseHelper dbHelper;

    private Spell_RecyclerViewAdapter adapter;
    private ArrayList<SpellModel> spellModels;
    private AlertDialog dialog;
    private SearchView searchView;

    private boolean ableToDelete;

    private int currentCharacterId;
    private int classImage;

    public static BrowseSpellsFragment newInstance( int currentCharacterId,
                                                    boolean ableToDelete,
                                                    SpellClickListener listener) {
        BrowseSpellsFragment fragment = new BrowseSpellsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.spellClickListener = listener;
        fragment.ableToDelete = ableToDelete;
        fragment.currentCharacterId = currentCharacterId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_spells, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        CardView cardView = view.findViewById(R.id.empty_browse_card);

        dbHelper = new DatabaseHelper(getContext());

        if (currentCharacterId >= 0) {
            int casterClassId = dbHelper.getCharacterById(currentCharacterId).getClassId();
            classImage = dbHelper.getClassById(casterClassId).getClassImage();
            spellModels = dbHelper.getSpellsByCharacterId(currentCharacterId);
        } else {
            spellModels = dbHelper.getAllSpells();
            classImage = R.drawable.big_book;
        }

        adapter = new Spell_RecyclerViewAdapter(
                getContext(), dbHelper, spellModels, classImage, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(spellModels.isEmpty())
            cardView.setVisibility(View.VISIBLE);
        else
            cardView.setVisibility((View.GONE));

        searchView = view.findViewById(R.id.searchView);
        FrameLayout searchViewContainer = view.findViewById(R.id.searchViewContainer);
        FloatingActionButton fabFilter = view.findViewById(R.id.fabFilter);
        FloatingActionButton fabSearch = view.findViewById(R.id.fabSearch);

        fabFilter.setOnClickListener(v -> showFilterDialog());

        searchViewContainer.setOnClickListener(v -> searchView.setIconified(false));

        fabSearch.setOnClickListener(v -> {
            if (searchViewContainer.getVisibility() == View.GONE) {
                searchViewContainer.setVisibility(View.VISIBLE);
                fabFilter.setVisibility(View.VISIBLE);
            } else {
                searchViewContainer.setVisibility(View.GONE);
                fabFilter.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        return view;
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        RadioGroup radioGroupRitual = dialogView.findViewById(R.id.radioGroupRitual);
        RadioGroup radioGroupConcentration = dialogView.findViewById(R.id.radioGroupConcentration);
        RadioGroup radioGroupV = dialogView.findViewById(R.id.radioGroupV);
        RadioGroup radioGroupS = dialogView.findViewById(R.id.radioGroupS);
        RadioGroup radioGroupM = dialogView.findViewById(R.id.radioGroupM);
        LinearLayout schoolCheckboxContainer = dialogView.findViewById(R.id.schoolCheckboxContainer);
        CheckBox[] checkBoxes = new CheckBox[10];
        for (int i = 0; i <= 9; i++) {
            @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier("checkBoxLevel" + i, "id", requireContext().getPackageName());
            checkBoxes[i] = dialogView.findViewById(resId);
        }

        ArrayList<SchoolModel> schoolModels = dbHelper.getAllSchools();
        for (SchoolModel school : schoolModels) {
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT );

            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(school.getName());
            checkBox.setChecked(true);
            checkBox.setPadding(16, 16, 16, 16);
            params.setMargins(0, 8, 0, 8);
            checkBox.setLayoutParams(params);

            schoolCheckboxContainer.addView(checkBox);
        }

        Button buttonApplyFilter = dialogView.findViewById(R.id.buttonApplyFilter);

        buttonApplyFilter.setOnClickListener(v -> {
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
        });

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), SpellCard_Activity.class);
        Bundle bundle = new Bundle();

        bundle.putInt("CLASS_IMAGE", classImage);
        bundle.putSerializable("SPELL", spellModels.get(position));

        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        if (spellClickListener != null && ableToDelete) {
            spellClickListener.onSpellLongClick(spellModels.get(position).getId());
            Toast.makeText(getContext(), "Spell removed", Toast.LENGTH_SHORT).show();
        }
    }
}