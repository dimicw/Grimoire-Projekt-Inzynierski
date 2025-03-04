package com.example.grimoire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.activities.SpellCardActivity;
import com.example.grimoire.dialogs.DeleteConfirmDialog;
import com.example.grimoire.dialogs.HelpDialog;
import com.example.grimoire.interfaces.CharacterInteractionListener;
import com.example.grimoire.interfaces.SpellClickListener;
import com.example.grimoire.models.CasterClassModel;
import com.example.grimoire.models.CharacterModel;
import com.example.grimoire.models.ChosenSpellModel;
import com.example.grimoire.fragments.AddCharacterFragment;
import com.example.grimoire.fragments.BrowseSpellsFragment;
import com.example.grimoire.fragments.ChangeCharacterFragment;
import com.example.grimoire.models.SpellModel;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        CharacterInteractionListener,
        SpellClickListener,
        AddCharacterFragment.SaveCharacterListener {

    private int currentCharacterId;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerName, headerClass;
    private ImageView headerImage;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new DatabaseHelper(this);
        if ( dbHelper.getAllCharacters().size() == 0
                || dbHelper.getAllSpells().size() == 0
                || dbHelper.getAllSchools().size() == 0
                || dbHelper.getAllClasses().size() == 0 ) {

            dbHelper.copyDatabaseFromAssets();
            dbHelper.copyDatabaseContent();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        View headerView;
        ActionBarDrawerToggle toggle;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        headerView = navigationView.getHeaderView(0);
        headerName = headerView.findViewById(R.id.header_name);
        headerClass = headerView.findViewById(R.id.header_class);
        headerImage = headerView.findViewById(R.id.header_image);

        if(savedInstanceState == null) {
            changeCharacter(dbHelper.getAllCharacters().get(0).getId());
            openBrowseSpells(false , false);
        }

        ImageButton buttonHelp = navigationView.findViewById(R.id.fabHelp);

        buttonHelp.setOnClickListener(v -> {
            HelpDialog helpDialog = new HelpDialog(this);
            helpDialog.show();
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_browse_spells)
            openBrowseSpells(false, false);

        else if (item.getItemId() == R.id.nav_add_spell)
            openBrowseSpells(false, true);

        else if (item.getItemId() == R.id.nav_switch_character)
            openChangeCharacter();

        else if (item.getItemId() == R.id.nav_add_character)
            openAddCharacter();

        else if (item.getItemId() == R.id.nav_add_nonclass_spell)
            openBrowseSpells(true, true);

        else if (item.getItemId() == R.id.nav_browse_all_spells)
            openBrowseSpells(true, false);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    private void openBrowseSpells(boolean allSpells, boolean addSpell) {
        int characterId = allSpells ? -1 : currentCharacterId;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                BrowseSpellsFragment.newInstance(characterId,
                        addSpell,this)).commit();

        navigationView.setCheckedItem(R.id.nav_browse_spells);
    }

    private void openAddCharacter() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddCharacterFragment.newInstance(this)).commit();
    }

    private void openChangeCharacter() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                ChangeCharacterFragment.newInstance(this)).commit();
    }


    @Override
    public void onCharacterClick(int characterId) {
        changeCharacter(characterId);
        openBrowseSpells(false, false);
    }

    @Override
    public void onCharacterLongClick(int characterId) {
        dbHelper.removeCharacterById(characterId);

        if(currentCharacterId == characterId)
            changeCharacter(dbHelper.getAllCharacters().get(0).getId());

        openChangeCharacter();
    }


    @Override
    public void onAddSpellClick(SpellModel spellModel) {
        ChosenSpellModel chosenSpellModel = new ChosenSpellModel(spellModel.getId(), currentCharacterId);

        if (dbHelper.checkChosenSpellDuplicates(chosenSpellModel))
            Toast.makeText(this, "You already have this spell", Toast.LENGTH_SHORT).show();
        else {
            dbHelper.addChosenSpell(chosenSpellModel);
            Toast.makeText(this, "Spell '" + spellModel.getName() + "' added", Toast.LENGTH_SHORT).show();
        }

        openBrowseSpells(false, false);

    }

    @Override
    public void onOpenSpellCardClick(int casterClassId, SpellModel spellModel) {
        Intent intent = new Intent(this, SpellCardActivity.class);
        Bundle bundle = new Bundle();

        int classImage = (casterClassId >= 0) ? dbHelper.getClassById(casterClassId).getClassImage() : R.drawable.spell_book;

        bundle.putInt("CLASS_IMAGE", classImage);
        bundle.putSerializable("SPELL", spellModel);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onDeleteSpellClick(SpellModel spellModel) {
        String name = spellModel.getName();

        DeleteConfirmDialog dialog = new DeleteConfirmDialog(
                                    this, name, true, confirmed -> {
            if (confirmed) {
                dbHelper.removeChosenSpell(currentCharacterId, spellModel.getId());
                openBrowseSpells(false, false);
                Toast.makeText(this,
                        "Spell '" + name + "' removed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void changeCharacter(int id) {
        CharacterModel characterModel = dbHelper.getCharacterById(id);
        CasterClassModel casterClassModel = dbHelper.getClassById(characterModel.getClassId());

        currentCharacterId = id;

        Objects.requireNonNull(getSupportActionBar())
                .setTitle(characterModel.getName());

        headerName.setText(characterModel.getName());
        headerClass.setText(casterClassModel.getName());
        headerImage.setImageResource(casterClassModel.getClassImage());
    }


    @Override
    public void onSaveButtonListener(CharacterModel characterModel) {
        int id = dbHelper.addCharacter(characterModel);
        changeCharacter(id);
        openBrowseSpells(false, false);
    }
}