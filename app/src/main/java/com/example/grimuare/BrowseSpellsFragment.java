package com.example.grimuare;

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

import java.util.ArrayList;


public class BrowseSpellsFragment extends Fragment implements RecyclerViewInterface {
    public interface SpellClickListener {
        void onSpellLongClick(int position);
    }

    private SpellClickListener spellClickListener;

    Spell_RecyclerViewAdapter adapter;

    ArrayList<ChosenSpell> chosenSpells;
    int[] classImages;

    RecyclerView recyclerView;
    CardView cardView;

    boolean ableToDelete;

    public static BrowseSpellsFragment newInstance(ArrayList<ChosenSpell> chosenSpells,
                                                   int[] classImages, boolean ableToDelete,
                                                   SpellClickListener listener) {
        BrowseSpellsFragment fragment = new BrowseSpellsFragment();
        Bundle args = new Bundle();
        args.putSerializable("chosenSpells", chosenSpells);
        args.putIntArray("classImages", classImages);
        fragment.setArguments(args);
        fragment.spellClickListener = listener;
        fragment.ableToDelete = ableToDelete;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_spells, container, false);

        assert getArguments() != null;
        chosenSpells = (ArrayList<ChosenSpell>) getArguments().getSerializable("chosenSpells");
        classImages = getArguments().getIntArray("classImages");

        recyclerView = view.findViewById(R.id.recyclerView);
        cardView = view.findViewById(R.id.empty_browse_card);

        adapter = new Spell_RecyclerViewAdapter(
                getContext(), chosenSpells, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(chosenSpells.size() == 0)
            cardView.setVisibility(View.VISIBLE);
        else
            cardView.setVisibility((View.GONE));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), SpellCard_Activity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("SPELL", chosenSpells.get(position));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        if (spellClickListener != null && ableToDelete){
            chosenSpells.remove(position);
            adapter.notifyItemRemoved(position);
            spellClickListener.onSpellLongClick(position);
            Toast.makeText(getContext(), "Spell removed", Toast.LENGTH_SHORT).show();
        }
    }
}