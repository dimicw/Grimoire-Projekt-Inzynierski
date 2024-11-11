package com.example.grimoire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.models.CasterClassModel;
import com.example.grimoire.models.CharacterModel;
import com.example.grimoire.models.ChosenSpellModel;
import com.example.grimoire.fragments.AddCharacter_Fragment;
import com.example.grimoire.fragments.AddSpell_Fragment;
import com.example.grimoire.fragments.BrowseSpellsFragment;
import com.example.grimoire.fragments.ChangeCharacter_Fragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ChangeCharacter_Fragment.CharacterInteractionListener,
        AddSpell_Fragment.SpellClickListener,
        BrowseSpellsFragment.SpellClickListener,
        AddCharacter_Fragment.SaveCharacterListener {

    private int currentCharacterId;

    // views and layouts
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerName, headerClass;
    private ImageView headerImage;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize SQLite Database Helper
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

        // Set up toolbar and navbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Find navigation bar header
        headerView = navigationView.getHeaderView(0);
        headerName = headerView.findViewById(R.id.header_name);
        headerClass = headerView.findViewById(R.id.header_class);
        headerImage = headerView.findViewById(R.id.header_image);

        // Open app on browsing spells
        if(savedInstanceState == null) {
            changeCharacter(dbHelper.getAllCharacters().get(0).getId());
            openBrowseSpells();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_browse_spells)
            openBrowseSpells();

        else if (item.getItemId() == R.id.nav_add_spell)
            openAddSpell(false);


        else if (item.getItemId() == R.id.nav_switch_character)
            openChangeCharacter();

        else if (item.getItemId() == R.id.nav_add_character)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddCharacter_Fragment.newInstance(this)).commit();

        else if (item.getItemId() == R.id.nav_add_nonclass_spell)
            openAddSpell(true);

        else if (item.getItemId() == R.id.nav_browse_all_spells)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    BrowseSpellsFragment.newInstance(-1, false, this)).commit();

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


    private void openBrowseSpells() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                BrowseSpellsFragment.newInstance(currentCharacterId,
                        true, this)).commit();
        navigationView.setCheckedItem(R.id.nav_browse_spells);
    }

    @Override
    public void onCharacterClick(int characterId) {
        changeCharacter(characterId);
        openBrowseSpells();
    }

    @Override
    public void onCharacterLongClick(int characterId) {
        dbHelper.removeCharacterById(characterId);

        if(currentCharacterId == characterId)
            changeCharacter(dbHelper.getAllCharacters().get(0).getId());

        openChangeCharacter();
    }

    @Override
    public void onAddSpellClick(int spellId) {
        ChosenSpellModel chosenSpellModel = new ChosenSpellModel(spellId, currentCharacterId);

        if (dbHelper.checkChosenSpellDuplicates(chosenSpellModel))
            Toast.makeText(this, "You already have this spell", Toast.LENGTH_SHORT).show();
        else
            dbHelper.addChosenSpell(chosenSpellModel);

        openBrowseSpells();

        System.out.println(dbHelper.getAllChosenSpells().toString());
    }

    @Override
    public void onSpellLongClick(int spellId) {
        dbHelper.removeChosenSpell(currentCharacterId, spellId);
        openBrowseSpells();
    }

    @Override
    public void onSaveButtonListener(CharacterModel characterModel) {
        int id = dbHelper.addCharacter(characterModel);
        changeCharacter(id);
        openBrowseSpells();
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

    private void openChangeCharacter() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                ChangeCharacter_Fragment.newInstance(this)).commit();
    }

    private void openAddSpell(boolean showAll) {
        int classId = showAll ? -1 : dbHelper.getCharacterById(currentCharacterId).getClassId();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                AddSpell_Fragment.newInstance(classId, this)).commit();
    }
}