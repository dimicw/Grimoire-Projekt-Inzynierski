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

    private ArrayList<Spell> spells;

    private int classId;

    public static AddSpell_Fragment newInstance(int classId, SpellClickListener listener) {
        AddSpell_Fragment fragment = new AddSpell_Fragment();
        fragment.spellClickListener = listener;
        fragment.classId = classId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_spell, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        spells = (classId >= 0) ? dbHelper.getSpellsAvailableForClass(classId) : dbHelper.getAllSpells();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        Spell_RecyclerViewAdapter adapter = new Spell_RecyclerViewAdapter(
                getContext(), dbHelper, spells, R.drawable.big_book, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        System.out.println(spells.get(position).toString());
        if (spellClickListener != null)
            spellClickListener.onAddSpellClick(spells.get(position).getId());
    }

    @Override
    public void onItemLongClick(int position) {

        //todo:!--------------------------------------------------------------------------------------------------------
        Intent intent = new Intent(getContext(), SpellCard_Activity.class);
        Bundle bundle = new Bundle();

        //ChosenSpell chosenSpell = new ChosenSpell(position, 0);

        bundle.putSerializable("SPELL", spells.get(position));
        intent.putExtras(bundle);

        startActivity(intent);
    }
}