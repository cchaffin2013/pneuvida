package com.example.christopher.pneuvida;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.insert(TABLE_PATIENTS, null, values);

        db.close();
    }

    //delete row
    public void deletePatient(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_NAME + "=\"" + name + "\";" );
        db.close();
    }

    /*name to string
    public String nameToString(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_DOB + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_NAME + "=\"" + name + "\";";
        String dob = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        dob = c.getString(c.getColumnIndex("_dob"));
        return dob;
    }*/

    //dob to string
    public String dobToString(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_DOB + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_NAME + "=\"" + name + "\";";
        String dob = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        dob = c.getString(c.getColumnIndex("_dob"));
        return dob;
    }

    //destroy database
    public void destroy() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
    }
}
