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
    private static final String COLUMN_RR = "_rr";
    private static final String COLUMN_OS = "_os";
    private static final String COLUMN_HR = "_hr";
    private static final String COLUMN_TEMP = "_temp";

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
                COLUMN_NOTES + " TEXT, " +
                COLUMN_RR + " TEXT, " +
                COLUMN_OS + " TEXT, " +
                COLUMN_HR + " TEXT, " +
                COLUMN_TEMP + " TEXT " +
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
        values.put(COLUMN_RR, "");
        values.put(COLUMN_OS, "");
        values.put(COLUMN_HR, "");
        values.put(COLUMN_TEMP, "");
        db.insert(TABLE_PATIENTS, null, values);

        db.close();
    }

    //delete row based on id
    public void deletePatient(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";" );
        db.close();
    }

    //delete row based on name
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

    //respiratory rate to int
    public String getRR(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_RR + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String rr;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        rr = c.getString(c.getColumnIndex("_rr"));
        db.close();
        c.close();
        return rr;
    }

    //oxygen saturation to int
    public String getOS(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_OS + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String os;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        os = c.getString(c.getColumnIndex("_os"));
        db.close();
        c.close();
        return os;
    }

    //heart rate to int
    public String getHR(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_HR + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String hr;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        hr = c.getString(c.getColumnIndex("_hr"));
        db.close();
        c.close();
        return hr;
    }

    //temperature to string
    public String getTemp(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_TEMP + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String temp = "";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        temp = c.getString(c.getColumnIndex("_temp"));
        db.close();
        c.close();
        return temp;
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

    public void setRR(List<String> data, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        String query = "SELECT " + COLUMN_RR + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";" ;
        String vitalsData;
        String value = "";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        sb.append(c.getString(c.getColumnIndex(COLUMN_RR)));
        for(int i = 0; i < data.size(); i++) {
            if(i == 0) {
                sb.append("\n");
            }
            value = data.get(i);
            sb.append(value);
            if(i != data.size() - 1) {
                if((i+1) % 2 == 0) {
                    sb.append(",");
                    sb.append("\n");
                } else {
                    sb.append(",");
                }
            } else {
                sb.append("\n");
            }

        }

        vitalsData = sb.toString();
        values.put(COLUMN_RR, vitalsData);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
        c.close();
    }

    public void setOS(List<String> data, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        String query = "SELECT " + COLUMN_OS + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";" ;
        String vitalsData;
        String value;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        sb.append(c.getString(c.getColumnIndex(COLUMN_OS)));
        for(int i = 0; i < data.size(); i++) {
            if(i == 0) {
                sb.append("\n");
            }
            value = data.get(i);
            sb.append(value);
            if(i != data.size() - 1) {
                if((i+1) % 2 == 0) {
                    sb.append(",");
                    sb.append("\n");
                } else {
                    sb.append(",");
                }
            } else {
                sb.append("\n");
            }
        }

        vitalsData = sb.toString();
        values.put(COLUMN_OS, vitalsData);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
        c.close();
    }

    public void setHR(List<String> data, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        String query = "SELECT " + COLUMN_HR + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";" ;
        String vitalsData;
        String value = "";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        sb.append(c.getString(c.getColumnIndex(COLUMN_HR)));
        for(int i = 0; i < data.size(); i++) {
            if(i == 0) {
                sb.append("\n");
            }
            value = data.get(i);
            sb.append(value);
            if(i != data.size() - 1) {
                if((i+1) % 2 == 0) {
                    sb.append(",");
                    sb.append("\n");
                } else {
                    sb.append(",");
                }
            } else {
                sb.append("\n");
            }
        }

        vitalsData = sb.toString();
        values.put(COLUMN_HR, vitalsData);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
        c.close();
    }

    public void setTemp(List<String> data, int id) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        String query = "SELECT " + COLUMN_TEMP + " FROM " + TABLE_PATIENTS + " WHERE " + COLUMN_ID + "=\"" + id + "\";" ;
        String vitalsData;
        String value = "";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        sb.append(c.getString(c.getColumnIndex(COLUMN_TEMP)));
        for(int i = 0; i < data.size(); i++) {
            if(i == 0) {
                sb.append("\n");
            }
            value = data.get(i);
            sb.append(value);
            if(i != data.size() - 1) {
                if((i+1) % 2 == 0) {
                    sb.append(",");
                    sb.append("\n");
                } else {
                    sb.append(",");
                }
            } else {
                sb.append("\n");
            }

        }

        vitalsData = sb.toString();
        values.put(COLUMN_TEMP, vitalsData);
        db.update(TABLE_PATIENTS, values, "_id=" + id, null);
        db.close();
        c.close();
    }

    //database IDs to List
    public List toList() {
        List<Integer> patientIDs = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_PATIENTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()) {//loops through db and adds patient names to list
            patientIDs.add(c.getInt(c.getColumnIndex("_id")));
            c.moveToNext();
        }
        db.close();
        c.close();
        return patientIDs;
    }

    //destroy database
    public void destroy() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);
        db.close();
    }
}
