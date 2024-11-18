package com.example.grimoire.Helpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.grimoire.models.CasterClassModel;
import com.example.grimoire.models.CharacterModel;
import com.example.grimoire.models.ChosenSpellModel;
import com.example.grimoire.models.SchoolModel;
import com.example.grimoire.models.SpellModel;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    private final Context context;
    private static final String DATABASE_NAME = "grimoire.db";
    private static final int DATABASE_VERSION = 82;
    private static final String SOURCE_DATABASE = "sqlite5.db";


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
    private static final String COLUMN_SPELLS_AT_HIGHER_LEVELS = "at_higher_levels";


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
                        COLUMN_SPELLS_DESCRIPTION + " TEXT, " +
                        COLUMN_SPELLS_AT_HIGHER_LEVELS + " TEXT);";
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
            assert context != null;
            InputStream input = context.getAssets().open("databases/" + SOURCE_DATABASE);
            String outFileName = context.getDatabasePath(SOURCE_DATABASE).getPath();
            File databaseFolder = new File(outFileName).getParentFile();

            assert databaseFolder != null;
            if (!databaseFolder.exists()) {
                if (databaseFolder.mkdirs())
                    Log.d("DatabaseHelper", "Directory  created successfully");
                else
                    Log.e("DatabaseHelper", "Error creating Directory ");
            }

            OutputStream output = Files.newOutputStream(Paths.get(outFileName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0)
                output.write(buffer, 0, length);

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
            grimoireDb.execSQL("DETACH DATABASE sourceDb");

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


    public int addCharacter(CharacterModel characterModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHARACTERS_NAME, characterModel.getName());
        values.put(COLUMN_CHARACTERS_CLASS_ID, characterModel.getClassId());
        int id = (int) db.insert(TABLE_CHARACTERS, null, values);
        db.close();
        return id;
    }

    public void addChosenSpell(ChosenSpellModel chosenSpellModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHOSEN_SPELL_ID, chosenSpellModel.getSpellId());
        values.put(COLUMN_CHOSEN_CHARACTER_ID, chosenSpellModel.getCharacterId());
        db.insert(TABLE_CHOSEN_SPELLS, null, values);
        db.close();
    }


    @SuppressLint("Range")
    public ArrayList<SpellModel> getAllSpells() {
        ArrayList<SpellModel> spellModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SPELLS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SpellModel spellModel = new SpellModel();
                spellModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
                spellModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
                spellModel.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
                spellModel.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
                spellModel.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
                spellModel.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
                spellModel.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
                spellModel.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
                spellModel.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
                spellModel.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
                spellModel.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
                spellModel.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
                spellModel.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
                spellModel.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));
                spellModel.setAtHigherLevels(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_AT_HIGHER_LEVELS)));
                spellModelList.add(spellModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spellModelList;
    }

    @SuppressLint("Range")
    public ArrayList<SpellModel> getSpellsByCharacterId(int characterId) {
        ArrayList<SpellModel> spellModelList = new ArrayList<>();
        String selectQuery = "SELECT s.* FROM " + TABLE_SPELLS + " s " +
                "INNER JOIN " + TABLE_CHOSEN_SPELLS + " cs " +
                "ON s." + COLUMN_SPELLS_ID +" = cs." + COLUMN_CHOSEN_SPELL_ID +
                " WHERE cs." + COLUMN_CHOSEN_CHARACTER_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(characterId)});

        if (cursor.moveToFirst()) {
            do {
                SpellModel spellModel = new SpellModel();
                spellModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
                spellModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
                spellModel.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
                spellModel.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_SCHOOL_ID)));
                spellModel.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
                spellModel.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
                spellModel.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
                spellModel.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
                spellModel.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
                spellModel.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
                spellModel.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
                spellModel.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
                spellModel.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
                spellModel.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));
                spellModel.setAtHigherLevels(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_AT_HIGHER_LEVELS)));

                spellModelList.add(spellModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spellModelList;
    }

    @SuppressLint("Range")
    public ArrayList<SpellModel> getSpellsAvailableForClass(int classId) {
        ArrayList<SpellModel> spellModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*" +
                " FROM " + TABLE_SPELLS + " s" +
                " JOIN " + TABLE_CLASS_AVAILABILITIES + " ca" +
                " ON s." + COLUMN_SPELLS_ID + " = ca." + COLUMN_AVAILABILITIES_SPELL_ID +
                " WHERE ca." + COLUMN_AVAILABILITIES_CLASS_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classId)});
        if (cursor.moveToFirst()) {
            do {
                SpellModel spellModel = new SpellModel();
                spellModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_ID)));
                spellModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_NAME)));
                spellModel.setLevel(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_LEVEL)));
                spellModel.setSchoolId(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_SCHOOL_ID)));
                spellModel.setCastingTime(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_CASTING_TIME)));
                spellModel.setRitual(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_RITUAL)) == 1);
                spellModel.setRange(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_RANGE)));
                spellModel.setComponents(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_COMPONENTS)));
                spellModel.setV(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_V)) == 1);
                spellModel.setS(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_S)) == 1);
                spellModel.setM(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_M)) == 1);
                spellModel.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DURATION)));
                spellModel.setConcentration(cursor.getInt(cursor.getColumnIndex(COLUMN_SPELLS_CONCENTRATION)) == 1);
                spellModel.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_DESCRIPTION)));
                spellModel.setAtHigherLevels(cursor.getString(cursor.getColumnIndex(COLUMN_SPELLS_AT_HIGHER_LEVELS)));
                spellModels.add(spellModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spellModels;
    }

    @SuppressLint("Range")
    public ArrayList<SchoolModel> getAllSchools() {
        ArrayList<SchoolModel> schoolModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SCHOOLS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SchoolModel schoolModel = new SchoolModel();
                schoolModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
                schoolModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOLS_NAME)));
                schoolModelList.add(schoolModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return schoolModelList;
    }

    @SuppressLint("Range")
    public SchoolModel getSchoolById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SCHOOLS + " WHERE " + COLUMN_SCHOOLS_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            SchoolModel schoolModel = new SchoolModel();
            schoolModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
            schoolModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOLS_NAME)));
            cursor.close();
            db.close();
            return schoolModel;
        } else {
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public SchoolModel getSchoolByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SCHOOLS + " WHERE " + COLUMN_SCHOOLS_NAME + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});

        if (cursor != null && cursor.moveToFirst()) {
            SchoolModel schoolModel = new SchoolModel();
            schoolModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOLS_ID)));
            schoolModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOLS_NAME)));
            cursor.close();
            db.close();
            return schoolModel;
        } else {
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<CasterClassModel> getAllClasses() {
        ArrayList<CasterClassModel> casterClassModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CLASSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CasterClassModel casterClassModel = new CasterClassModel();
                casterClassModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CLASSES_ID)));
                casterClassModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CLASSES_NAME)));
                casterClassModelList.add(casterClassModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return casterClassModelList;
    }

    @SuppressLint("Range")
    public CasterClassModel getClassById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CLASSES + " WHERE " + COLUMN_CLASSES_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            CasterClassModel casterClassModel = new CasterClassModel();
            casterClassModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CLASSES_ID)));
            casterClassModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CLASSES_NAME)));
            cursor.close();
            db.close();
            return casterClassModel;
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
    public ArrayList<CharacterModel> getAllCharacters() {
        ArrayList<CharacterModel> characterModelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHARACTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CharacterModel characterModel = new CharacterModel();
                characterModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHARACTERS_ID)));
                characterModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTERS_NAME)));
                characterModel.setClassId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTERS_CLASS_ID))));
                characterModelList.add(characterModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return characterModelList;
    }

    @SuppressLint("Range")
    public CharacterModel getCharacterById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHARACTERS + " WHERE " + COLUMN_CHARACTERS_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            CharacterModel characterModel = new CharacterModel();
            characterModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHARACTERS_ID)));
            characterModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTERS_NAME)));
            characterModel.setClassId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHARACTERS_CLASS_ID)));
            cursor.close();
            db.close();
            return characterModel;
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

    public boolean checkChosenSpellDuplicates(ChosenSpellModel chosenSpellModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CHOSEN_SPELLS +
                " WHERE " + COLUMN_CHOSEN_SPELL_ID + " = ?" +
                " AND " + COLUMN_CHOSEN_CHARACTER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chosenSpellModel.getSpellId()), String.valueOf(chosenSpellModel.getCharacterId())});
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
}

