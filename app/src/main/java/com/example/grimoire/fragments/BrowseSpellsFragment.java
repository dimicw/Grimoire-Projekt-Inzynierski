package com.example.grimoire.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.dialogs.FilterDialog;
import com.example.grimoire.interfaces.SpellClickListener;
import com.example.grimoire.models.SpellModel;
import com.example.grimoire.interfaces.RecyclerViewInterface;
import com.example.grimoire.adapters.SpellRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class BrowseSpellsFragment extends Fragment implements RecyclerViewInterface {


    private SpellClickListener spellClickListener;

    private DatabaseHelper dbHelper;

    private SpellRecyclerViewAdapter adapter;
    private ArrayList<SpellModel> spellModels;
    private SearchView searchView;

    private boolean addSpell;
    private int characterId;
    private int casterClassId;

    public static BrowseSpellsFragment newInstance( int currentCharacterId,
                                                    boolean addSpell,
                                                    SpellClickListener listener) {
        BrowseSpellsFragment fragment = new BrowseSpellsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.spellClickListener = listener;
        fragment.addSpell = addSpell;
        fragment.characterId = currentCharacterId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_spells, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        CardView cardView = view.findViewById(R.id.empty_browse_card);

        dbHelper = new DatabaseHelper(getContext());


        casterClassId = (characterId >= 0) ? dbHelper.getCharacterById(characterId).getClassId() : -1;
        int classImage = (addSpell || characterId < 0) ? R.drawable.spell_book : dbHelper.getClassById(casterClassId).getClassImage();

        if (characterId <= 0)
            spellModels = dbHelper.getAllSpells();
        else if (addSpell)
            spellModels = dbHelper.getSpellsAvailableForClass(casterClassId);
        else
            spellModels = dbHelper.getSpellsByCharacterId(characterId);


        adapter = new SpellRecyclerViewAdapter(
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
        FilterDialog filterDialog = new FilterDialog(requireContext(), dbHelper, adapter, searchView);
        filterDialog.show();
    }

    @Override
    public void onItemClick(int position) {
        if (spellClickListener != null) {
            if (addSpell)
                spellClickListener.onAddSpellClick(spellModels.get(position));
            else
                spellClickListener.onOpenSpellCardClick(casterClassId, spellModels.get(position));
        }
    }

    @Override
    public void onItemLongClick(int position) {
        if (addSpell)
            spellClickListener.onOpenSpellCardClick(casterClassId, spellModels.get(position));
        else if (characterId >= 0 && spellClickListener != null)
            spellClickListener.onDeleteSpellClick(spellModels.get(position));
    }
}