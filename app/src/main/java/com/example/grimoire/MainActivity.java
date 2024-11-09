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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ChangeCharacter_Fragment.CharacterInteractionListener,
        AddSpell_Fragment.SpellClickListener,
        BrowseSpellsFragment.SpellClickListener,
        AddCharacter_Fragment.SaveCharacterListener {

    // arrays from database
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

        // initialize SQLite Database Helper
        dbHelper = new DatabaseHelper(this);

        // TODO: DELETE!!! --------------------------------------------
        /*Character c1 = new Character("New Character", 7);
        dbHelper.addCharacter(c1);

        Character c2 = new Character("New Character 2", 0);
        dbHelper.addCharacter(c2);

        Spell s1 = new Spell("name", 0, 0, "castingTime",
                true, "range", "components", true, false, true, "duration", true, ":)");
        s1.setId(0);
        dbHelper.addSpell(s1);

        Spell s2 = new Spell("name2", 3, 0, "castingTime2",
                true, "range2", "components2", true, false, true, "duration2", true, "Opis testowy");
        s2.setId(1);
        dbHelper.addSpell(s2);

        CasterClass cc1 = new CasterClass(0, "bard");
        dbHelper.addClass(cc1);

        CasterClass cc2 = new CasterClass(7, "wizard");
        dbHelper.addClass(cc2);

        School sc1 = new School("Evocation");
        School sc2 = new School("Divination");

        dbHelper.addSchool(sc1);
        dbHelper.addSchool(sc2);

        ChosenSpell cs1 = new ChosenSpell(0,0);
        ChosenSpell cs2 = new ChosenSpell(1,0);

        dbHelper.addChosenSpell(cs1);
        dbHelper.addChosenSpell(cs2);*/
        // todo: ------------------------------------------------------


        allSpells = dbHelper.getAllSpells();
        allCharacters = dbHelper.getAllCharacters();
        allCasterClasses = dbHelper.getAllClasses();
        allSchools = dbHelper.getAllSchools();
        allClassAvailabilities = dbHelper.getAllClassAvailabilities();

        // add starting character
       /* if(allCharacters.size() <= 1) {
            Character newCharacter = new Character("New Character", 7);
            dbHelper.addCharacter(newCharacter);
            allCharacters = dbHelper.getAllCharacters();
        }*/


        // set up toolbar and navbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // find navbar header
        headerView = navigationView.getHeaderView(0);
        headerName = headerView.findViewById(R.id.header_name);
        headerClass = headerView.findViewById(R.id.header_class);
        headerImage = headerView.findViewById(R.id.header_image);

        // open app on browsing spells
        if(savedInstanceState == null) {
            changeCharacter(0);
            openBrowseSpells();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_browse_spells)
            openBrowseSpells();
        else if(item.getItemId() == R.id.nav_add_spell) {
            /*ArrayList<ChosenSpell> allAsChosen = new ArrayList<>();
            for (Spell spell : allSpells)
                allAsChosen.add(new ChosenSpell(spell, R.drawable.big_book));

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddSpell_Fragment.newInstance(allAsChosen, this)).commit();*/
        }
        else if(item.getItemId() == R.id.nav_switch_character)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ChangeCharacter_Fragment.newInstance(allCharacters, this)).commit();
        else if(item.getItemId() == R.id.nav_add_character)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddCharacter_Fragment.newInstance(this)).commit();
        else if(item.getItemId() == R.id.nav_add_nonclass_spell) {
            // TODO: add class restrictions
        }
        else if(item.getItemId() == R.id.nav_browse_all_spells) {
            /*chosenSpells = new ArrayList<>();
            for(Spell spell : allSpells)
                chosenSpells.add(new ChosenSpell(spell, R.drawable.big_book));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    BrowseSpellsFragment.newInstance(chosenSpells, classImages, false, this)).commit();*/
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
        /*chosenSpells = new ArrayList<>();
        for (int i = 0; i < allSpells.size(); i++)
            for (BoundSpell boundSpell : allCharacters.get(currentCharacterId).getBoundSpells())
                if (i == boundSpell.getSpellId())
                    chosenSpells.add(
                            new ChosenSpell(allSpells.get(i), boundSpell.getSpellImage())
                    );*/
        //chosenSpells = dbHelper.getAllChosenSpells();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                BrowseSpellsFragment.newInstance(//chosenSpells, //classImages,
                        true, this)).commit();
        navigationView.setCheckedItem(R.id.nav_browse_spells);
    }

    /*private static ArrayList<Spell> parseSpellXML(InputStream inputStream) {
        ArrayList<Spell> spellsList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList spellNodes = document.getElementsByTagName("spell");
            for (int i = 0; i < spellNodes.getLength(); i++) {
                Element spellElement = (Element) spellNodes.item(i);

                NodeList descriptionNodes = spellElement.getElementsByTagName("description");
                String[] descriptionStrings = new String[descriptionNodes.getLength()];

                for(int j = 0; j < descriptionNodes.getLength(); j++) {
                    Element descriptionElement = (Element) descriptionNodes.item(j);
                    descriptionStrings[j] = descriptionElement.getTextContent();
                }

                Spell spell = new Spell(
                    spellElement.getElementsByTagName("name").item(0).getTextContent(),
                    spellElement.getElementsByTagName("source").item(0).getTextContent(),
                    Integer.parseInt(spellElement.getElementsByTagName("level").item(0).getTextContent()),
                    spellElement.getElementsByTagName("school").item(0).getTextContent(),
                    spellElement.getElementsByTagName("castingTime").item(0).getTextContent(),
                    Boolean.parseBoolean(spellElement.getElementsByTagName("ritual").item(0).getTextContent()),
                    spellElement.getElementsByTagName("range").item(0).getTextContent(),
                    spellElement.getElementsByTagName("components").item(0).getTextContent(),
                    Boolean.parseBoolean(spellElement.getElementsByTagName("v").item(0).getTextContent()),
                    Boolean.parseBoolean(spellElement.getElementsByTagName("s").item(0).getTextContent()),
                    Boolean.parseBoolean(spellElement.getElementsByTagName("m").item(0).getTextContent()),
                    spellElement.getElementsByTagName("duration").item(0).getTextContent(),
                    Boolean.parseBoolean(spellElement.getElementsByTagName("concentration").item(0).getTextContent()),
                    descriptionStrings
                );

                spellsList.add(spell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return spellsList;
    }*/

    @Override
    public void onCharacterClick(int position) {
        changeCharacter(position);
        openBrowseSpells();
    }

    @Override
    public void onCharacterLongClick(int position) {
        if (currentCharacterId == position) {
            if (position != 0)
                changeCharacter(0);
            else
                changeCharacter(1);
        }
        allCharacters.remove(position);
        //saveCharactersToFile();
    }

    @Override
    public void onAddSpellClick(int position) {
        /*boolean hasSpell = false;

        for (BoundSpell boundSpell : allCharacters.get(currentCharacterId).getBoundSpells())
            if (position == boundSpell.getSpellId()) {
                hasSpell = true;
                break;
            }

        if (hasSpell)
            Toast.makeText(this, "You already have this spell", Toast.LENGTH_SHORT).show();
        else {
            allCharacters.get(currentCharacterId)
                    .addSpell(position, allCharacters.get(currentCharacterId).getImage()); //TODO: change image
            saveCharactersToFile();
        }*/

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
        /*allCharacters.add(character);
        saveCharactersToFile();

        changeCharacter(allCharacters.size() - 1);
        openBrowseSpells();*/
    }

    private void changeCharacter(int id) {
        currentCharacterId = id;
        Objects.requireNonNull(getSupportActionBar())
                .setTitle(allCharacters.get(currentCharacterId).getName());

        headerName.setText(allCharacters.get(currentCharacterId).getName());
        headerClass.setText("123"); //allCharacters.get(currentCharacterId).getClassId());
        headerImage.setImageResource(R.drawable.big_book); //allCharacters.get(currentCharacterId).getImage());
    }

    /*private void saveCharactersToFile() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(allCharacters);

            Toast.makeText(this, "Changes saved", Toast.LENGTH_LONG).show();

            oos.close();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (oos != null)
                    oos.close();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadCharactersFromFile() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = openFileInput(FILE_NAME);
            ois = new ObjectInputStream(fis);
            allCharacters = (ArrayList<Character>) ois.readObject();

            ois.close();
            fis.close();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if(fis != null)
                    fis.close();
                if(ois != null)
                    ois.close();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    
    private void loadCharactersFromDB() {

    }
}