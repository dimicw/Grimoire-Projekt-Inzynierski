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
import com.example.grimoire.classes.CasterClass;
import com.example.grimoire.classes.Character;
import com.example.grimoire.classes.ChosenSpell;
import com.example.grimoire.classes.ClassAvailability;
import com.example.grimoire.classes.School;
import com.example.grimoire.classes.Spell;
import com.example.grimoire.fragments.AddCharacter_Fragment;
import com.example.grimoire.fragments.AddSpell_Fragment;
import com.example.grimoire.fragments.BrowseSpellsFragment;
import com.example.grimoire.fragments.ChangeCharacter_Fragment;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ChangeCharacter_Fragment.CharacterInteractionListener,
        AddSpell_Fragment.SpellClickListener,
        BrowseSpellsFragment.SpellClickListener,
        AddCharacter_Fragment.SaveCharacterListener {

    // Arrays to store data from the database
    private ArrayList<Spell> allSpells = new ArrayList<>();
    private ArrayList<Character> allCharacters = new ArrayList<>();
    private ArrayList<CasterClass> allCasterClasses = new ArrayList<>();
    private ArrayList<School> allSchools = new ArrayList<>();
    private ArrayList<ClassAvailability> allClassAvailabilities = new ArrayList<>();
    private ArrayList<ChosenSpell> chosenSpells = new ArrayList<>();

    private int currentCharacterId;

    // views and layouts
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerName, headerClass;
    private ImageView headerImage;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        View headerView;
        ActionBarDrawerToggle toggle;

        // Initialize SQLite Database Helper
        dbHelper = new DatabaseHelper(this);
        if ( dbHelper.getAllCharacters().size() == 0
        || dbHelper.getAllSpells().size() == 0
        || dbHelper.getAllSchools().size() == 0
        || dbHelper.getAllClasses().size() == 0 ) {

            dbHelper.copyDatabaseFromAssets();
            dbHelper.copyDatabaseContent();

            Character secondCharacter = new Character("Gandalf2", 1);
            Character firstCharacter = new Character("Gandalf3", 1);
            CasterClass cl1 = new CasterClass("Bard");

            dbHelper.addClass(cl1);
            dbHelper.addCharacter(firstCharacter);
            dbHelper.addCharacter(secondCharacter);
        }

        allSpells = dbHelper.getAllSpells();
        allCharacters = dbHelper.getAllCharacters();
        allCasterClasses = dbHelper.getAllClasses();
        allSchools = dbHelper.getAllSchools();
        allClassAvailabilities = dbHelper.getAllClassAvailabilities();

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
            //changeCharacter(0);
            changeCharacter(dbHelper.getAllCharacters().get(0).getId());
            openBrowseSpells();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_browse_spells)
            openBrowseSpells();

        else if (item.getItemId() == R.id.nav_add_spell) {
            ArrayList<ChosenSpell> allAsChosen = new ArrayList<>();
            /*for (Spell spell : allSpells)
                allAsChosen.add(new ChosenSpell(spell, R.drawable.big_book));

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddSpell_Fragment.newInstance(allAsChosen, this)).commit();*/


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddSpell_Fragment.newInstance(this)).commit();
        }

        else if (item.getItemId() == R.id.nav_switch_character)
            openChangeCharacter();

        else if (item.getItemId() == R.id.nav_add_character)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddCharacter_Fragment.newInstance(this)).commit();

        else if (item.getItemId() == R.id.nav_add_nonclass_spell) {
            // TODO: add class restrictions
        }

        else if (item.getItemId() == R.id.nav_browse_all_spells) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    BrowseSpellsFragment.newInstance(-1, false, this)).commit();
        }

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
        ChosenSpell chosenSpell = new ChosenSpell(spellId, currentCharacterId);

        if (dbHelper.checkChosenSpellDuplicates(chosenSpell))
            Toast.makeText(this, "You already have this spell", Toast.LENGTH_SHORT).show();
        else
            dbHelper.addChosenSpell(chosenSpell);

        openBrowseSpells();
    }

    @Override
    public void onSpellLongClick(int position) {
        /*ArrayList<BoundSpell> boundSpells = allCharacters.get(currentCharacterId).getBoundSpells();
        boundSpells.remove(position);
        allCharacters.get(currentCharacterId).setBoundSpells(boundSpells);

        saveCharactersToFile();*/
    }

    @Override
    public void onSaveButtonListener(Character character) {
        int id = dbHelper.addCharacter(character);
        changeCharacter(id);
        openBrowseSpells();
    }

    private void changeCharacter(int id) {
        Character character = dbHelper.getCharacterById(id);
        CasterClass casterClass = dbHelper.getClassById(character.getClassId());

        currentCharacterId = id;

        Objects.requireNonNull(getSupportActionBar())
                .setTitle(character.getName());

        headerName.setText(character.getName());
        headerClass.setText(casterClass.getName());
        headerImage.setImageResource(casterClass.getClassImage());
    }

    private void openChangeCharacter() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                ChangeCharacter_Fragment.newInstance(this)).commit();
    }
}