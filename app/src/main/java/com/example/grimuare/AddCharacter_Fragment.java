package com.example.grimuare;

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


public class AddCharacter_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public interface SaveCharacterListener {
        void onSaveButtonListener(Character character);
    }

    private SaveCharacterListener saveCharacterListener;

    private EditText nameField;

    private String className;

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

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.class_names, R.layout.spinner_item);

        nameField = view.findViewById(R.id.name_et);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        saveButton.setOnClickListener(view1 -> saveCharacterListener.onSaveButtonListener(
                new Character(nameField.getText().toString(), className)));

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        className = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}