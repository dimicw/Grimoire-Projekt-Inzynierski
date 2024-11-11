package com.example.grimoire.fragments;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.CasterClassModel;
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
    private CasterClassModel casterClassModel;
    private ArrayList<SpellModel> spellModels;

    private RecyclerView recyclerView;
    private CardView cardView;
    private AlertDialog dialog;

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

        dbHelper = new DatabaseHelper(getContext());

        if (currentCharacterId >= 0) {
            int casterClassId = dbHelper.getCharacterById(currentCharacterId).getClassId();
            classImage = dbHelper.getClassById(casterClassId).getClassImage();
            spellModels = dbHelper.getSpellsByCharacterId(currentCharacterId);
        } else {
            spellModels = dbHelper.getAllSpells();
            classImage = R.drawable.big_book;
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        cardView = view.findViewById(R.id.empty_browse_card);

        adapter = new Spell_RecyclerViewAdapter(
                getContext(), dbHelper, spellModels, classImage, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(spellModels.isEmpty())
            cardView.setVisibility(View.VISIBLE);
        else
            cardView.setVisibility((View.GONE));

        FloatingActionButton fabFilter = view.findViewById(R.id.fabFilter);
        fabFilter.setOnClickListener(v -> showFilterDialog());

        return view;
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        SearchView searchViewDialog = dialogView.findViewById(R.id.searchViewDialog);
        Button buttonApplyFilter = dialogView.findViewById(R.id.buttonApplyFilter);

        searchViewDialog.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty())
                    adapter.filter("");
                else
                    adapter.filter(newText);
                return false;
            }
        });

        buttonApplyFilter.setOnClickListener(v -> {
            // Apply any additional filters if needed
            // For now, just dismiss the dialog
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