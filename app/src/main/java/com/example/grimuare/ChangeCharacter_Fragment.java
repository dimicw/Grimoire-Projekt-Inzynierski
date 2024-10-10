package com.example.grimuare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class ChangeCharacter_Fragment extends Fragment implements RecyclerViewInterface {

    public interface CharacterInteractionListener {
        void onCharacterClick(int position);
        void onCharacterLongClick(int position);
    }

    private Character_RecyclerViewAdapter adapter;

    private CharacterInteractionListener characterInteractionListener;

    private ArrayList<Character> allCharacters;

    private RecyclerView recyclerView;

    public static ChangeCharacter_Fragment newInstance(ArrayList<Character> allCharacters,
                                                       CharacterInteractionListener listener) {
        ChangeCharacter_Fragment fragment = new ChangeCharacter_Fragment();
        Bundle args = new Bundle();
        args.putSerializable("allCharacters", allCharacters);
        fragment.setArguments(args);
        fragment.characterInteractionListener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_character, container, false);

        assert getArguments() != null;
        allCharacters = (ArrayList<Character>) getArguments().getSerializable("allCharacters");

        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new Character_RecyclerViewAdapter(
                getContext(), allCharacters, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        if (characterInteractionListener != null)
            characterInteractionListener.onCharacterClick(position);
    }


    @Override
    public void onItemLongClick(int position) {
        if (characterInteractionListener != null) {
            if (position >= 0 && position < allCharacters.size() && allCharacters.size() > 1) {
                allCharacters.remove(position);
                adapter.notifyItemRemoved(position);
                characterInteractionListener.onCharacterLongClick(position);
                Toast.makeText(getContext(), "Character removed", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "Cannot delete your last character", Toast.LENGTH_SHORT).show();
        }
    }
}