package com.example.appointmentbookingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "fullname";
    private static final String COL_EMAIL = "email";
    private static final String COL_AGE = "age";
    private static final String COL_GENDER = "gender";
    private static final String COL_STATE = "state";
    private static final String COL_PASSWORD = "password";

    // Doctors table
    private static final String TABLE_DOCTORS = "doctors";
    private static final String COL_DOCTOR_ID = "doctor_id";
    private static final String COL_DOCTOR_NAME = "doctor_name";
    private static final String COL_SPECIALITY = "speciality";
    private static final String COL_RATING = "rating";
    private static final String COL_EXPERIENCE = "experience";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table (your original structure)
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_EMAIL + " TEXT UNIQUE, "
                + COL_AGE + " INTEGER, "
                + COL_GENDER + " TEXT, "
                + COL_STATE + " TEXT, "
                + COL_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create doctors table (new table for dashboard functionality)
        String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + "("
                + COL_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_DOCTOR_NAME + " TEXT,"
                + COL_SPECIALITY + " TEXT,"
                + COL_RATING + " REAL,"
                + COL_EXPERIENCE + " TEXT)";
        db.execSQL(CREATE_DOCTORS_TABLE);

        // Insert sample doctors
        insertSampleDoctors(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);

        // Create tables again
        onCreate(db);
    }

    // Your original user registration methods
    public boolean registerUser(String fullname, String email, int age, String gender, String state, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (userExists(email)) {
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, fullname);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_AGE, age);
        contentValues.put(COL_GENDER, gender);
        contentValues.put(COL_STATE, state);
        contentValues.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public boolean userExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{email, password});
        boolean valid = (cursor.getCount() > 0);
        cursor.close();
        return valid;
    }

    // New methods for dashboard functionality
    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_NAME};
        String selection = COL_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String fullName = "";
        if (cursor.moveToFirst()) {
            fullName = cursor.getString(0);
        }
        cursor.close();
        return fullName;
    }

    public Cursor getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DOCTORS, null, null, null, null, null, null);
    }

    // Helper method to insert sample doctors
    private void insertSampleDoctors(SQLiteDatabase db) {
        ContentValues doctor1 = new ContentValues();
        doctor1.put(COL_DOCTOR_NAME, "Dr. Michael Roberts");
        doctor1.put(COL_SPECIALITY, "Orthopedics");
        doctor1.put(COL_RATING, 4.2);
        doctor1.put(COL_EXPERIENCE, "20 Year");
        db.insert(TABLE_DOCTORS, null, doctor1);

        ContentValues doctor2 = new ContentValues();
        doctor2.put(COL_DOCTOR_NAME, "Dr. Sarah Thompson");
        doctor2.put(COL_SPECIALITY, "Cardiology");
        doctor2.put(COL_RATING, 4.5);
        doctor2.put(COL_EXPERIENCE, "4 Year");
        db.insert(TABLE_DOCTORS, null, doctor2);
    }
}
