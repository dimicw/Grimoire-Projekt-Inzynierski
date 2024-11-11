package com.example.grimoire.Helpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.grimoire.classes.CasterClass;
import com.example.grimoire.classes.Character;
import com.example.grimoire.classes.ChosenSpell;
import com.example.grimoire.classes.ClassAvailability;
import com.example.grimoire.classes.School;
import com.example.grimoire.classes.Spell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    private final Context context;
    private static final String DATABASE_NAME = "grimoire.db";
    private static final int DATABASE_VERSION = 78;
    private static final String SOURCE_DATABASE = "sqlite4.db";


    // table spells
    private static final String TABLE_SPELLS = "spells";
    private static final String COLUMN_SPELLS_ID = "_id";
    private static final String COLUMN_SPELLS_NAME = "name";
    private static final String COLUMN_SPELLS_LEVEL = "level";
    private static final String COLUMN_SPELLS_SCHOOL_ID = "school_id";
    private static final String COLUMN_SPELLS_CASTING_TIME = "casting_time";
    private static final String COLUMN_SPELLS_RITUAL = "ritual";
    private static final String COLUMN_SPELLS_RANGE = "range";
    private static final String COLUMN_SPELLS_COMPONENTS = "components";
    private static final String COLUMN_SPELLS_V = "v";
    private static final String COLUMN_SPELLS_S = "s";
    private static final String COLUMN_SPELLS_M = "m";
    private static final String COLUMN_SPELLS_DURATION = "duration";
    private static final String COLUMN_SPELLS_CONCENTRATION = "concentration";
    private static final String COLUMN_SPELLS_DESCRIPTION = "description";


    // table schools
    private static final String TABLE_SCHOOLS = "schools";
    private static final String COLUMN_SCHOOLS_ID = "_id";
    private static final String COLUMN_SCHOOLS_NAME = "name";


    // table classes
    private static final String TABLE_CLASSES = "classes";
    private static final String COLUMN_CLASSES_ID = "_id";
    private static final String COLUMN_CLASSES_NAME = "name";


    // table characters
    private static final String TABLE_CHARACTERS = "characters";
    private static final String COLUMN_CHARACTERS_ID = "_id";
    private static final String COLUMN_CHARACTERS_NAME = "name";
    private static final String COLUMN_CHARACTERS_CLASS_ID = "class_id";


    // table class_availabilities
    private static final String TABLE_CLASS_AVAILABILITIES = "class_availabilities";
    private static final String COLUMN_AVAILABILITIES_ID = "_id";
    private static final String COLUMN_AVAILABILITIES_SPELL_ID = "spell_id";
    private static final String COLUMN_AVAILABILITIES_CLASS_ID = "class_id";


    // table chosen_spells
    private static final String TABLE_CHOSEN_SPELLS = "chosen_spells";
    private static final String COLUMN_CHOSEN_ID = "_id";
    private static final String COLUMN_CHOSEN_SPELL_ID = "spell_id";
    private static final String COLUMN_CHOSEN_CHARACTER_ID = "character_id";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSpellsTable =
                "CREATE TABLE IF NOT EXISTS " + TABLE_SPELLS + " (" +
                        COLUMN_SPELLS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SPELLS_NAME + " TEXT, " +
                        COLUMN_SPELLS_LEVEL + " INTEGER, " +
                        COLUMN_SPELLS_SCHOOL_ID + " INTEGER, " +
                        COLUMN_SPELLS_CASTING_TIME + " TEXT, " +
                        COLUMN_SPELLS_RITUAL + " INTEGER DEFAULT 0, " +
                        COLUMN_SPELLS_RANGE + " TEXT, " +
                        COLUMN_SPELLS_COMPONENTS + " TEXT, " +
                        COLUMN_SPELLS_V + " INTEGER DEFAULT 0, " +
                        COLUMN_SPELLS_S + " INTEGER DEFAULT 0, " +
                        COLUMN_SPELLS_M + " INTEGER DEFAULT 0, " +
                        COLUMN_SPELLS_DURATION + " TEXT, " +
                        COLUMN_SPELLS_CONCENTRATION + " INTEGER DEFAULT 0, " +
                        COLUMN_SPELLS_DESCRIPTION + " TEXT);";
        db.execSQL(createSpellsTable);

        String createSchoolsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHOOLS + " (" +
                COLUMN_SCHOOLS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCHOOLS_NAME + " TEXT);";
        db.execSQL(createSchoolsTable);

        String createClassesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASSES + " (" +
                COLUMN_CLASSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLASSES_NAME + " TEXT);";
        db.execSQL(createClassesTable);

        String createCharactersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CHARACTERS + " (" +
                COLUMN_CHARACTERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CHARACTERS_NAME + " TEXT, " +
                COLUMN_CHARACTERS_CLASS_ID + " INTEGER);";
        db.execSQL(createCharactersTable);

        String createClassAvailabilitiesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASS_AVAILABILITIES + " (" +
                COLUMN_AVAILABILITIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AVAILABILITIES_SPELL_ID + " INTEGER, " +
                COLUMN_AVAILABILITIES_CLASS_ID + " INTEGER);";
        db.execSQL(createClassAvailabilitiesTable);

        String createChosenSpellsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CHOSEN_SPELLS + " (" +
                COLUMN_CHOSEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CHOSEN_SPELL_ID + " INTEGER, " +
                COLUMN_CHOSEN_CHARACTER_ID + " INTEGER);";
        db.execSQL(createChosenSpellsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPELLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOOLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_AVAILABILITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHOSEN_SPELLS);
        onCreate(db);
    }


    public void copyDatabaseFromAssets() {
        try {
            System.out.println("get asset");
            assert context != null;
            InputStream input = context.getAssets().open("databases/" + SOURCE_DATABASE);
            System.out.println("getDBPath");
            String outFileName = context.getDatabasePath(SOURCE_DATABASE).getPath();
            System.out.println("get parent folder");
            File databaseFolder = new File(outFileName).getParentFile();
            assert databaseFolder != null;
            if (!databaseFolder.exists()) {
                databaseFolder.mkdirs();
            }
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
            Log.d("DatabaseHelper", "Database copied successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error copying database", e);
        }
    }

    public void copyDatabaseContent() {
        SQLiteDatabase grimoireDb = null;
        SQLiteDatabase sourceDatabase = null;
        try {
            // Open the source database
            assert context != null;
            String sourceDBPath = context.getDatabasePath(SOURCE_DATABASE).getPath();
            System.out.println(sourceDBPath);
            sourceDatabase = SQLiteDatabase.openDatabase(sourceDBPath, null, SQLiteDatabase.OPEN_READONLY);

            // Open the destination database
            grimoireDb = this.getWritableDatabase();

            // Attach the source database to the destination database
            grimoireDb.execSQL("ATTACH DATABASE '" + sourceDBPath + "' AS sourceDb");

            // Copy data from sourceDb to the destination database
            grimoireDb.beginTransaction();
            grimoireDb.execSQL("INSERT INTO spells SELECT * FROM sourceDb.spells");
            grimoireDb.execSQL("INSERT INTO schools SELECT * FROM sourceDb.schools");
            grimoireDb.execSQL("INSERT INTO classes SELECT * FROM sourceDb.classes");
            grimoireDb.execSQL("INSERT INTO characters SELECT * FROM sourceDb.characters");
            grimoireDb.execSQL("INSERT INTO class_availabilities SELECT * FROM sourceDb.class_availabilities");
            grimoireDb.execSQL("INSERT INTO chosen_spells SELECT * FROM sourceDb.chosen_spells");
            grimoireDb.setTransactionSuccessful();
            grimoireDb.endTransaction();

            // Detach the source database
            //grimoireDb.execSQL("DETACH DATABASE sourceDb");

            Log.d("DatabaseHelper", "Database content copied successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error copying database content", e);
        } finally {
            if (sourceDatabase != null) {
                sourceDatabase.close();
            }
            if (grimoireDb != null) {
                grimoireDb.close();
            }
        }
    }


    public void addSpell(Spell spell) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SPELLS_NAME, spell.getName());
        values.put(COLUMN_SPELLS_LEVEL, spell.getLevel());
        values.put(COLUMN_SPELLS_SCHOOL_ID, spell.getSchoolId());
        values.put(COLUMN_SPELLS_CASTING_TIME, spell.getCastingTime());
        values.put(COLUMN_SPELLS_RITUAL, spell.isRitual() ? 1 : 0);
        values.put(COLUMN_SPELLS_RANGE, spell.getRange());
        values.put(COLUMN_SPELLS_COMPONENTS, spell.getComponents());
        values.put(COLUMN_SPELLS_V, spell.isV() ? 1 : 0);
        values.put(COLUMN_SPELLS_S, spell.isS() ? 1 : 0);
        values.put(COLUMN_SPELLS_M, spell.isM() ? 1 : 0);
        values.put(COLUMN_SPELLS_DURATION, spell.getDuration());
        values.put(COLUMN_SPELLS_CONCENTRATION, spell.isConcentration() ? 1 : 0);
        values.put(COLUMN_SPELLS_DESCRIPTION, spell.getDescription());
        db.insert(TABLE_SPELLS, null, values);
        db.close();
    }

    public void addSchool(School school) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHOOLS_NAME, school.getName());
        db.insert(TABLE_SCHOOLS, null, values);
        db.close();
    }

    public void addClass(CasterClass casterClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASSES_NAME, casterClass.getName());
        db.insert(TABLE_CLASSES, null, values);
        db.close();
    }

    public int addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHARACTERS_NAME, character.getName());
        values.put(COLUMN_CHARACTERS_CLASS_ID, character.getClassId());
        int id = (int) db.insert(TABLE_CHARACTERS, null, values);
        db.close();
        return id;
    }

    public void addChosenSpell(ChosenSpell chosenSpell) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHOSEN_SPELL_ID, chosenSpell.getSpellId());
        values.put(COLUMN_CHOSEN_CHARACTER_ID, chosenSpell.getCharacterId());
        db.insert(TABLE_CHOSEN_SPELLS, null, values);
        db.close();
    }

    public void addClassAvailability(ClassAvailability classAvailability) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AVAILABILITIES_CLASS_ID, classAvailability.getSpellId());
        values.put(COLUMN_AVAILABILITIES_CLASS_ID, classAvailability.getClassId());
        db.insert(TABLE_CLASS_AVAILABILITIES, null, values);
        db.close();
    }


    @SuppressLint("Range")
    public ArrayList<Spell> getAllSpells() {
        ArrayList<Spell> spellList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SPELLS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Spell spell = new Spell();
                spell.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
                spell.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
                spell.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
                spell.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
                spell.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
                spell.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
                spell.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
                spell.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
                spell.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
                spell.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
                spell.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
                spell.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
                spell.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
                spell.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));
                spellList.add(spell);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spellList;
    }

    @SuppressLint("Range")
    public Spell getSpellById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SPELLS + " WHERE " + COLUMN_SPELLS_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Spell spell = new Spell();
            spell.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
            spell.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
            spell.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
            spell.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_SCHOOL_ID)));
            spell.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
            spell.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
            spell.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
            spell.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
            spell.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
            spell.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
            spell.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
            spell.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
            spell.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
            spell.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));
            cursor.close();
            db.close();
            return spell;
        } else {
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Spell> getSpellsByCharacterId(int characterId) {
        ArrayList<Spell> spellList = new ArrayList<>();
        String selectQuery = "SELECT s.* FROM " + TABLE_SPELLS + " s " +
                "INNER JOIN " + TABLE_CHOSEN_SPELLS + " cs " +
                "ON s." + COLUMN_SPELLS_ID +" = cs." + COLUMN_CHOSEN_SPELL_ID +
                " WHERE cs." + COLUMN_CHOSEN_CHARACTER_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(characterId)});

        if (cursor.moveToFirst()) {
            do {
                Spell spell = new Spell();
                spell.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
                spell.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
                spell.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
                spell.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_SCHOOL_ID)));
                spell.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
                spell.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
                spell.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
                spell.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
                spell.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
                spell.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
                spell.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
                spell.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
                spell.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
                spell.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));

                spellList.add(spell);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spellList;
    }

    @SuppressLint("Range")
    public ArrayList<Spell> getSpellsAvailableForClass(int classId) {
        ArrayList<Spell> spells = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*" +
                " FROM " + TABLE_SPELLS + " s" +
                " JOIN " + TABLE_CLASS_AVAILABILITIES + " ca" +
                " ON s." + COLUMN_SPELLS_ID + " = ca." + COLUMN_AVAILABILITIES_SPELL_ID +
                " WHERE ca." + COLUMN_AVAILABILITIES_CLASS_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classId)});
        if (cursor.moveToFirst()) {
            do {
                Spell spell = new Spell();
                spell.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
                spell.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
                spell.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
                spell.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_SCHOOL_ID)));
                spell.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
                spell.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
                spell.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
                spell.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
                spell.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
                spell.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
                spell.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
                spell.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
                spell.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
                spell.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));
                spells.add(spell);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spells;
    }

    @SuppressLint("Range")
    public ArrayList<School> getAllSchools() {
        ArrayList<School> schoolList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SCHOOLS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                School school = new School();
                school.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
                school.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOLS_NAME)));
                schoolList.add(school);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return schoolList;
    }

    @SuppressLint("Range")
    public School getSchoolById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SCHOOLS + " WHERE " + COLUMN_SCHOOLS_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            School school = new School();
            school.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
            school.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOLS_NAME)));
            cursor.close();
            db.close();
            return school;
        } else {
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<CasterClass> getAllClasses() {
        ArrayList<CasterClass> casterClassList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CLASSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CasterClass casterClass = new CasterClass();
                casterClass.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CLASSES_ID)));
                casterClass.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CLASSES_NAME)));
                casterClassList.add(casterClass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return casterClassList;
    }

    @SuppressLint("Range")
    public CasterClass getClassById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CLASSES + " WHERE " + COLUMN_CLASSES_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            CasterClass casterClass = new CasterClass();
            casterClass.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CLASSES_ID)));
            casterClass.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CLASSES_NAME)));
            cursor.close();
            db.close();
            return casterClass;
        } else {
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllClassNames() {
        ArrayList<String> casterClassNames = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_CLASSES_NAME +
                " FROM " + TABLE_CLASSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name;
                name = cursor.getString(cursor.getColumnIndex(COLUMN_CLASSES_NAME));
                casterClassNames.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return casterClassNames;
    }

    @SuppressLint("Range")
    public int getClassIdByClassName(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CLASSES_ID +
                " FROM " + TABLE_CLASSES +
                " WHERE " + COLUMN_CLASSES_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{className});
        int classId = -1;
        if (cursor.moveToFirst()) {
            classId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASSES_ID));
        }
        cursor.close();
        db.close();
        return classId;
    }

    @SuppressLint("Range")
    public ArrayList<Character> getAllCharacters() {
        ArrayList<Character> characterList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHARACTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Character character = new Character();
                character.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHARACTERS_ID)));
                character.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTERS_NAME)));
                character.setClassId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTERS_CLASS_ID))));
                characterList.add(character);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return characterList;
    }

    @SuppressLint("Range")
    public Character getCharacterById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHARACTERS + " WHERE " + COLUMN_CHARACTERS_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Character character = new Character();
            character.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHARACTERS_ID)));
            character.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTERS_NAME)));
            character.setClassId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHARACTERS_CLASS_ID)));
            cursor.close();
            db.close();
            return character;
        } else {
            db.close();
            return null;
        }
    }

    public void removeCharacterById(int characterId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();

            // Delete character's spellbook from chosen_spells table
            db.delete(TABLE_CHOSEN_SPELLS, COLUMN_CHOSEN_CHARACTER_ID + " = ?",
                    new String[]{String.valueOf(characterId)});

            // Delete character from characters table
            db.delete(TABLE_CHARACTERS, COLUMN_CHARACTERS_ID + " = ?",
                    new String[]{String.valueOf(characterId)});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting character and chosen spells", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<ChosenSpell> getAllChosenSpells() {
        ArrayList<ChosenSpell> chosenSpellList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHOSEN_SPELLS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ChosenSpell chosenSpell = new ChosenSpell();
                chosenSpell.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHOSEN_ID)));
                chosenSpell.setSpellId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CHOSEN_SPELL_ID))));
                chosenSpell.setCharacterId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CHOSEN_CHARACTER_ID))));
                chosenSpellList.add(chosenSpell);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chosenSpellList;
    }

    public boolean checkChosenSpellDuplicates(ChosenSpell chosenSpell) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CHOSEN_SPELLS +
                " WHERE " + COLUMN_CHOSEN_SPELL_ID + " = ?" +
                " AND " + COLUMN_CHOSEN_CHARACTER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chosenSpell.getSpellId()), String.valueOf(chosenSpell.getCharacterId())});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public void removeChosenSpell(int characterId, int spellId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_CHOSEN_SPELLS, COLUMN_CHOSEN_CHARACTER_ID + " = ? " +
                            "AND " + COLUMN_CHOSEN_SPELL_ID + " = ?",
                    new String[]{String.valueOf(characterId), String.valueOf(spellId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting chosen spell", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<ClassAvailability> getAllClassAvailabilities() {
        ArrayList<ClassAvailability> classAvailabilitiesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CLASS_AVAILABILITIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ClassAvailability classAvailability = new ClassAvailability();
                classAvailability.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHOSEN_ID)));
                classAvailability.setSpellId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CHOSEN_SPELL_ID))));
                classAvailabilitiesList.add(classAvailability);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return classAvailabilitiesList;
    }

}

