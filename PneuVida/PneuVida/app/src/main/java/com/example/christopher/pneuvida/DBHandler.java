package com.example.christopher.pneuvida;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "patientProfiles.db";
    //table names
    private static final String TABLE_PATIENTS = "patients";
    //column names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_DOB = "_dob";
    private static final String COLUMN_SEX = "_sex";
    private static final String COLUMN_HEIGHT = "_height";
    private static final String COLUMN_WEIGHT = "_weight";
    private static final String COLUMN_MEDS = "_meds";
    private static final String COLUMN_ALLERGIES = "_allergies";
    private static final String COLUMN_NOTES = "_notes";

    /* this dbhandler will be a singleton
    ** to ensure that only one instance of the db
    ** will exist at a time*/
    private static DBHandler singleton;
    public static synchronized DBHandler getDBHandler(Context context) {
        if (singleton == null) {
            singleton = new DBHandler(context.getApplicationContext(), DATABASE_NAME , null, 1);
        }
        return singleton;
    }

    private DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //creates database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_PATIENTS+ "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DOB + " TEXT, " +
                COLUMN_SEX + " TEXT, " +
                COLUMN_HEIGHT + " TEXT, " +
                COLUMN_WEIGHT + " TEXT, " +
                COLUMN_MEDS + " TEXT, " +
                COLUMN_ALLERGIES + " TEXT, " +
                COLUMN_NOTES + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
    }

    //add row
    public void addPatient(Patient patient) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_NAME, patient.get_name());
        values.put(COLUMN_DOB, patient.get_dob());
        values.put(COLUMN_SEX, patient.get_sex());
        values.put(COLUMN_HEIGHT, patient.get_height());
        values.put(COLUMN_WEIGHT, patient.get_weight());
        values.put(COLUMN_MEDS, patient.get_meds());
        values.put(COLUMN_ALLERGIES, patient.get_allergies());
        values.put(COLUMN_NOTES, patient.get_notes());
        db.insert(TABLE_PATIENTS, null, values);

        db.close();
    }

    //delete row
    public void deletePatient(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_NAME + "=\"" + name + "\";" );
        db.close();
    }

    //getters
    //name to string
    public String getName(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_NAME + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String name = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        name = c.getString(c.getColumnIndex("_name"));
        db.close();
        return name;
    }

    //id to int
    public int getID(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_ID + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_NAME + "=\"" + name + "\";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        int id = c.getInt(c.getColumnIndex("_id"));
        db.close();
        return id;
    }

    //dob to string
    public String getDOB(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_DOB + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String dob = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        dob = c.getString(c.getColumnIndex("_dob"));
        db.close();
        return dob;
    }

    //sex to string
    public String getSex(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_SEX + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String sex = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        sex = c.getString(c.getColumnIndex("_sex"));
        db.close();
        return sex;
    }

    //height to string
    public String getHeight(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_HEIGHT + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String height = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        height = c.getString(c.getColumnIndex("_height"));
        db.close();
        return height;
    }

    //weight to string
    public String getWeight(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_WEIGHT + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String weight = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        weight = c.getString(c.getColumnIndex("_weight"));
        db.close();
        return weight;
    }

    //meds to string
    public String getMeds(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_MEDS + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String meds = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        meds = c.getString(c.getColumnIndex("_meds"));
        db.close();
        return meds;
    }

    //allergies to string
    public String getAllergies(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_ALLERGIES + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String allergies = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        allergies = c.getString(c.getColumnIndex("_allergies"));
        db.close();
        return allergies;
    }

    //notes to string
    public String getNotes(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_NOTES + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String notes = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        notes = c.getString(c.getColumnIndex("_notes"));
        db.close();
        return notes;
    }

    //setters
    public void setName(String name, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_NAME, name);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setDob(String dob, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_DOB, dob);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setSex(String sex, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_SEX, sex);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setHeight(String height, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_HEIGHT, height);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setWeight(String weight, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_WEIGHT, weight);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setMeds(String meds, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_MEDS, meds);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setAllergies(String allergies, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_ALLERGIES, allergies);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    public void setNotes(String notes, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_NOTES, notes);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
    }

    //database names to List
    public List toList() {
        List<String> patientNames = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_PATIENTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()) {//loops through db and adds patient names to list
            if (c.getString(c.getColumnIndex("_name")) != null) {
                patientNames.add(c.getString(c.getColumnIndex("_name")));
            }
            c.moveToNext();
        }
        db.close();

        return patientNames;
    }

    //destroy database
    public void destroy() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
        db.close();
    }
}
