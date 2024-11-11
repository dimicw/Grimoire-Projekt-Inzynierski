package com.example.grimoire.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.models.CharacterModel;

import java.util.ArrayList;


public class AddCharacter_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public interface SaveCharacterListener {
        void onSaveButtonListener(CharacterModel characterModel);
    }

    private DatabaseHelper dbHelper;

    private SaveCharacterListener saveCharacterListener;

    private EditText nameField;

    private int classId;

    public static AddCharacter_Fragment newInstance(SaveCharacterListener listener) {
        AddCharacter_Fragment fragment = new AddCharacter_Fragment();
        fragment.saveCharacterListener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_character, container, false);
        Spinner spinner = view.findViewById(R.id.class_spinner);
        Button saveButton = view.findViewById(R.id.save_button);

        dbHelper = new DatabaseHelper(getContext());
        ArrayList<String> class_names = dbHelper.getAllClassNames();

        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter(getContext(), R.layout.spinner_item, class_names);


        nameField = view.findViewById(R.id.name_et);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        saveButton.setOnClickListener(view1 -> saveCharacterListener.onSaveButtonListener(
                new CharacterModel(nameField.getText().toString(), classId))); //className)));

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String className = adapterView.getItemAtPosition(i).toString();
        classId = dbHelper.getClassIdByClassName(className);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}