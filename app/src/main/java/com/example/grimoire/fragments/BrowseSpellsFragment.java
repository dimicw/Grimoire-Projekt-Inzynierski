package com.example.grimoire.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.classes.ChosenSpell;
import com.example.grimoire.classes.Spell;
import com.example.grimoire.interfaces.RecyclerViewInterface;
import com.example.grimoire.activities.SpellCard_Activity;
import com.example.grimoire.adapters.Spell_RecyclerViewAdapter;

import java.util.ArrayList;


public class BrowseSpellsFragment extends Fragment implements RecyclerViewInterface {
    public interface SpellClickListener {
        void onSpellLongClick(int position);
    }

    private SpellClickListener spellClickListener;

    DatabaseHelper dbHelper;

    Spell_RecyclerViewAdapter adapter;

    ArrayList<ChosenSpell> chosenSpells;
    ArrayList<Spell> spells;
    int[] classImages;

    RecyclerView recyclerView;
    CardView cardView;

    boolean ableToDelete;

    public static BrowseSpellsFragment newInstance(//ArrayList<ChosenSpell> chosenSpells,
                                                   //int[] classImages,
                                                   boolean ableToDelete,
                                                   SpellClickListener listener) {
        BrowseSpellsFragment fragment = new BrowseSpellsFragment();
        Bundle args = new Bundle();
        //args.putSerializable("chosenSpells", chosenSpells);
        //args.putIntArray("classImages", classImages);
        fragment.setArguments(args);
        fragment.spellClickListener = listener;
        fragment.ableToDelete = ableToDelete;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_spells, container, false);

        dbHelper = new DatabaseHelper(getContext());

        assert getArguments() != null;
        //chosenSpells = (ArrayList<ChosenSpell>) getArguments().getSerializable("chosenSpells");
        chosenSpells = dbHelper.getAllChosenSpells();
        spells = dbHelper.getSpellsByCharacterId(0);
        classImages = getArguments().getIntArray("classImages");

        recyclerView = view.findViewById(R.id.recyclerView);
        cardView = view.findViewById(R.id.empty_browse_card);

        adapter = new Spell_RecyclerViewAdapter(
                getContext(), spells, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(spells.isEmpty())
            cardView.setVisibility(View.VISIBLE);
        else
            cardView.setVisibility((View.GONE));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), SpellCard_Activity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("SPELL", spells.get(position));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        if (spellClickListener != null && ableToDelete){
            spells.remove(position);
            adapter.notifyItemRemoved(position);
            spellClickListener.onSpellLongClick(position);
            Toast.makeText(getContext(), "Spell removed", Toast.LENGTH_SHORT).show();
        }
    }
}