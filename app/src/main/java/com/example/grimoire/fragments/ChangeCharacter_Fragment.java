package com.example.grimoire.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.CasterClassModel;
import com.example.grimoire.models.CharacterModel;
import com.example.grimoire.interfaces.RecyclerViewInterface;
import com.example.grimoire.adapters.Character_RecyclerViewAdapter;

import java.util.ArrayList;


public class ChangeCharacter_Fragment extends Fragment implements RecyclerViewInterface {

    public interface CharacterInteractionListener {
        void onCharacterClick(int position);
        void onCharacterLongClick(int position);
    }

    private CharacterInteractionListener characterInteractionListener;

    private ArrayList<CharacterModel> allCharacterModels;

    public static ChangeCharacter_Fragment newInstance(
                                                       CharacterInteractionListener listener) {
        ChangeCharacter_Fragment fragment = new ChangeCharacter_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.characterInteractionListener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_character, container, false);
        ArrayList<CasterClassModel> allClasses;

        try (DatabaseHelper dbHelper = new DatabaseHelper(getContext())) {

            allCharacterModels = dbHelper.getAllCharacters();
            allClasses = dbHelper.getAllClasses();
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        Character_RecyclerViewAdapter adapter = new Character_RecyclerViewAdapter(
                getContext(), allCharacterModels, allClasses, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        if (characterInteractionListener != null)
            characterInteractionListener.onCharacterClick(allCharacterModels.get(position).getId());
    }

    @Override
    public void onItemLongClick(int position) {
        if (characterInteractionListener != null) {
            if (position >= 0 && position < allCharacterModels.size() && allCharacterModels.size() > 1) {
                int characterId = allCharacterModels.get(position).getId();
                characterInteractionListener.onCharacterLongClick(characterId);
                Toast.makeText(getContext(), "Character removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Cannot delete your only character", Toast.LENGTH_SHORT).show();
            }
        }
    }

}