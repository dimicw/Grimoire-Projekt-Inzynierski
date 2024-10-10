package com.example.grimuare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private static final String FILE_NAME = "characters";

    // arrays
    private ArrayList<Spell> allSpells = new ArrayList<>();
    private ArrayList<ChosenSpell> chosenSpells = new ArrayList<>();
    private ArrayList<Character> allCharacters = new ArrayList<>();

    private int currentCharacterId;

    // views and layouts
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView headerName, headerClass;
    private ImageView headerImage;

    private static final int[] classImages = {
            R.drawable.class_icon___artificer,  //0
            R.drawable.class_icon___bard,       //1
            R.drawable.class_icon___blood_mage, //2
            R.drawable.class_icon___cleric,     //3
            R.drawable.class_icon___druid,      //4
            R.drawable.class_icon___paladin,    //5
            R.drawable.class_icon___ranger,     //6
            R.drawable.class_icon___sorcerer,   //7
            R.drawable.class_icon___warlock,    //8
            R.drawable.class_icon___wizard      //9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        View headerView;
        ActionBarDrawerToggle toggle;

        // Load XML for parsing
        AssetManager assetManager = getAssets();
        InputStream inputStream;
        try {
            inputStream = assetManager.open("all_spells.xml");
            allSpells = parseSpellXML(inputStream);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        loadCharactersFromFile();
        if (allCharacters.size() <= 1)
            allCharacters.add(new Character("New Character", 10, 10, 10,
                   10, 10, 10, 1, "Wizard", classImages[8]));

        // setting up toolbar and navbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // finding navbar header
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
            ArrayList<ChosenSpell> allAsChosen = new ArrayList<>();
            for (Spell spell : allSpells)
                allAsChosen.add(new ChosenSpell(spell, R.drawable.big_book));

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    AddSpell_Fragment.newInstance(allAsChosen, this)).commit();
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
            chosenSpells = new ArrayList<>();
            for(Spell spell : allSpells)
                chosenSpells.add(new ChosenSpell(spell, R.drawable.big_book));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    BrowseSpellsFragment.newInstance(chosenSpells, classImages, false, this)).commit();
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
        chosenSpells = new ArrayList<>();
        for (int i = 0; i < allSpells.size(); i++)
            for (BoundSpell boundSpell : allCharacters.get(currentCharacterId).getBoundSpells())
                if (i == boundSpell.getSpellId())
                    chosenSpells.add(
                            new ChosenSpell(allSpells.get(i), boundSpell.getSpellImage())
                    );

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                BrowseSpellsFragment.newInstance(chosenSpells, classImages, true, this)).commit();
        navigationView.setCheckedItem(R.id.nav_browse_spells);
    }

    private static ArrayList<Spell> parseSpellXML(InputStream inputStream) {
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
    }

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
        saveCharactersToFile();
    }

    @Override
    public void onAddSpellClick(int position) {
        boolean hasSpell = false;

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
        }

        openBrowseSpells();
    }

    @Override
    public void onSpellLongClick(int position) {
        ArrayList<BoundSpell> boundSpells = allCharacters.get(currentCharacterId).getBoundSpells();
        boundSpells.remove(position);
        allCharacters.get(currentCharacterId).setBoundSpells(boundSpells);

        saveCharactersToFile();
    }

    @Override
    public void onSaveButtonListener(Character character) {
        allCharacters.add(character);
        saveCharactersToFile();

        changeCharacter(allCharacters.size() - 1);
        openBrowseSpells();
    }

    private void changeCharacter(int id) {
        currentCharacterId = id;
        Objects.requireNonNull(getSupportActionBar())
                .setTitle(allCharacters.get(currentCharacterId).getName());

        headerName.setText(allCharacters.get(currentCharacterId).getName());
        headerClass.setText(allCharacters.get(currentCharacterId).getMainClass());
        headerImage.setImageResource(allCharacters.get(currentCharacterId).getImage());
    }

    private void saveCharactersToFile() {
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
    }
}