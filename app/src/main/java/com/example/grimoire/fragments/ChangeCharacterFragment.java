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
import com.example.grimoire.dialogs.DeleteConfirmDialog;
import com.example.grimoire.interfaces.CharacterInteractionListener;
import com.example.grimoire.models.CasterClassModel;
import com.example.grimoire.models.CharacterModel;
import com.example.grimoire.interfaces.RecyclerViewInterface;
import com.example.grimoire.adapters.CharacterRecyclerViewAdapter;

import java.util.ArrayList;


public class ChangeCharacterFragment extends Fragment implements RecyclerViewInterface {

    private CharacterInteractionListener characterInteractionListener;

    private ArrayList<CharacterModel> allCharacterModels;

    public static ChangeCharacterFragment newInstance(CharacterInteractionListener listener) {
        ChangeCharacterFragment fragment = new ChangeCharacterFragment();
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

        CharacterRecyclerViewAdapter adapter = new CharacterRecyclerViewAdapter(
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
            if (position >= 0 && position < allCharacterModels.size() && allCharacterModels.size() > 1){
                CharacterModel characterModel = allCharacterModels.get(position);

                DeleteConfirmDialog dialog = new DeleteConfirmDialog(getContext(), characterModel.getName(), false, confirmed -> {
                    if (confirmed) {
                        int characterId = allCharacterModels.get(position).getId();
                        characterInteractionListener.onCharacterLongClick(characterId);
                        Toast.makeText(getContext(), "Character removed", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            } else
                Toast.makeText(getContext(), "Cannot delete your only character", Toast.LENGTH_SHORT).show();
        }
    }

}