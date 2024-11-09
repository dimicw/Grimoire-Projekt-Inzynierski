package com.example.grimoire.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.classes.ChosenSpell;
import com.example.grimoire.classes.Spell;
import com.example.grimoire.interfaces.RecyclerViewInterface;
import com.example.grimoire.activities.SpellCard_Activity;
import com.example.grimoire.adapters.Spell_RecyclerViewAdapter;

import java.util.ArrayList;


public class AddSpell_Fragment extends Fragment implements RecyclerViewInterface {

    public interface SpellClickListener {
        void onAddSpellClick(int position);
    }
    private SpellClickListener spellClickListener;

    ArrayList<ChosenSpell> allSpells;

    RecyclerView recyclerView;

    /*public static AddSpell_Fragment newInstance(ArrayList<ChosenSpell> allSpells,
                                                SpellClickListener listener) {
        AddSpell_Fragment fragment = new AddSpell_Fragment();
        Bundle args = new Bundle();
        args.putSerializable("allSpells", allSpells);
        fragment.setArguments(args);
        fragment.spellClickListener = listener;
        return fragment;
    }*/

    public static AddSpell_Fragment newInstance(SpellClickListener listener) {
        AddSpell_Fragment fragment = new AddSpell_Fragment();
        fragment.spellClickListener = listener;
        return fragment;
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_spell, container, false);

        assert getArguments() != null;
        allSpells = (ArrayList<ChosenSpell>) getArguments().getSerializable("allSpells");

        recyclerView = view.findViewById(R.id.recyclerView);

        Spell_RecyclerViewAdapter adapter = new Spell_RecyclerViewAdapter(
                getContext(), allSpells, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_spell, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        ArrayList<Spell> allSpells = dbHelper.getAllSpells();


        /*ArrayList<ChosenSpell> allAsChosen = new ArrayList<>();
        for (Spell spell : allSpells)
            allAsChosen.add(new ChosenSpell(spell, R.drawable.big_book));*/



        recyclerView = view.findViewById(R.id.recyclerView);

        Spell_RecyclerViewAdapter adapter = new Spell_RecyclerViewAdapter(
                getContext(), allSpells, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        if (spellClickListener != null)
            spellClickListener.onAddSpellClick(position);
    }

    @Override
    public void onItemLongClick(int position) {
        Intent intent = new Intent(getContext(), SpellCard_Activity.class);
        Bundle bundle = new Bundle();

        ChosenSpell chosenSpell = new ChosenSpell(position, 0);

        bundle.putSerializable("SPELL", chosenSpell);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}