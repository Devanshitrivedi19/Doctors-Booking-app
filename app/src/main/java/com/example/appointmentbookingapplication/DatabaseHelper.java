package com.example.appointmentbookingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Users.db";
    private static final int DATABASE_VERSION = 3; // Increased to force onUpgrade

    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "fullname";
    private static final String COL_EMAIL = "email";
    private static final String COL_AGE = "age";
    private static final String COL_GENDER = "gender";
    private static final String COL_STATE = "state";
    private static final String COL_PASSWORD = "password";

    // Doctors Table
    public static final String TABLE_DOCTORS = "doctors";
    public static final String COL_DOCTOR_ID = "doctor_id";
    public static final String COL_DOCTOR_NAME = "doctor_name";
    public static final String COL_SPECIALITY = "speciality";
    public static final String COL_RATING = "rating";
    public static final String COL_EXPERIENCE = "experience";

    public static final String TABLE_WAITLIST = "waitlist";
    public static final String COL_WAITLIST_ID = "id";
    public static final String COL_PATIENT_NAME = "patient_name";
    public static final String COL_PHONE = "phone";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create Users Table
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME + " TEXT, "
                    + COL_EMAIL + " TEXT UNIQUE, "
                    + COL_AGE + " INTEGER, "
                    + COL_GENDER + " TEXT, "
                    + COL_STATE + " TEXT, "
                    + COL_PASSWORD + " TEXT)");

            // Create Doctors Table
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DOCTORS + "("
                    + COL_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_DOCTOR_NAME + " TEXT, "
                    + COL_SPECIALITY + " TEXT, "
                    + COL_RATING + " REAL, "
                    + COL_EXPERIENCE + " TEXT)");

            // Add this to onCreate() after creating doctors table
            db.execSQL("CREATE TABLE IF NOT EXISTS appointments (" +
                    "appointment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "doctor_name TEXT," +
                    "speciality TEXT," +
                    "appointment_date TEXT," +
                    "appointment_time TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WAITLIST + "("
                    + COL_WAITLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PATIENT_NAME + " TEXT, "
                    + COL_DOCTOR_NAME + " TEXT, "
                    + COL_PHONE + " TEXT, "
                    + COL_EMAIL + " TEXT, "
                    + COL_DATE + " TEXT, "
                    + COL_TIME + " TEXT)");

            // Insert Sample Data if Table is Empty
            if (!doesTableHaveData(db, TABLE_DOCTORS)) {
                insertSampleDoctors(db);
            }

            Log.d("DatabaseHelper", "Tables created successfully.");
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error creating tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);

        // Drop and Recreate Tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAITLIST);
        db.execSQL("DROP TABLE IF EXISTS appointments");
        onCreate(db);
    }

    // Register User
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

    // Check if User Exists
    public boolean userExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Validate User Login
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{email, password});
        boolean isValid = (cursor.getCount() > 0);
        cursor.close();
        return isValid;
    }

    // Get User Full Name
    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_NAME}, COL_EMAIL + " = ?", new String[]{email}, null, null, null);
        String fullName = "";
        if (cursor.moveToFirst()) {
            fullName = cursor.getString(0);
        }
        cursor.close();
        return fullName;
    }

    // Get All Doctors
// Get All Doctors Safely
    public Cursor getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Check if 'doctors' table exists
            Cursor tableCursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                    new String[]{TABLE_DOCTORS}
            );

            if (tableCursor.getCount() == 0) {
                Log.w("DatabaseHelper", "Doctors table does not exist. No content available.");
                tableCursor.close();
                return null;  // or return an empty MatrixCursor
            }

            tableCursor.close();

            // Now attempt to get all doctors
            cursor = db.query(TABLE_DOCTORS, null, null, null, null, null, null);

            if (cursor.getCount() == 0) {
                Log.i("DatabaseHelper", "No content available in doctors table.");
            }

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while retrieving doctors: ", e);
        }

        return cursor;
    }

    private boolean doesTableHaveData(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        boolean hasData = false;
        if (cursor.moveToFirst()) {
            hasData = cursor.getInt(0) > 0;
        }
        cursor.close();
        return hasData;
    }
    // DatabaseHelper.java
    public Cursor getDoctorsBySpecialty(String specialty) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DOCTORS,
                null,
                COL_SPECIALITY + " = ?",
                new String[]{specialty},
                null, null, null);
    }

    public boolean addToWaitlist(String patientName, String doctorName, String phone, String email, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PATIENT_NAME, patientName);
        values.put(COL_DOCTOR_NAME, doctorName);  // Add doctor name
        values.put(COL_PHONE, phone);
        values.put(COL_EMAIL, email);
        values.put(COL_DATE, date);
        values.put(COL_TIME, time);
        long result = db.insert(TABLE_WAITLIST, null, values);
        return result != -1;
    }
    public Cursor getAllWaitlistedAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_WAITLIST, null, null, null, null, null, COL_DATE + " ASC");
    }
    // Insert Sample Doctors
    private void insertSampleDoctors(SQLiteDatabase db) {
        // Add Orthopedics doctor
        ContentValues doctor1 = new ContentValues();
        doctor1.put(COL_DOCTOR_NAME, "Dr. Sarah Johnson");
        doctor1.put(COL_SPECIALITY, "Orthopedics");
        doctor1.put(COL_RATING, 4.2);
        doctor1.put(COL_EXPERIENCE, "20 Years");
        db.insert(TABLE_DOCTORS, null, doctor1);

// Add Dentistry doctor
        ContentValues doctor2 = new ContentValues();
        doctor2.put(COL_DOCTOR_NAME, "Dr. Michael Roberts");
        doctor2.put(COL_SPECIALITY, "Dentistry");
        doctor2.put(COL_RATING, 4.5);
        doctor2.put(COL_EXPERIENCE, "15 Years");
        db.insert(TABLE_DOCTORS, null, doctor2);

// Add Neurology doctor
        ContentValues doctor3 = new ContentValues();
        doctor3.put(COL_DOCTOR_NAME, "Dr. David Chen");
        doctor3.put(COL_SPECIALITY, "Orthopedics");
        doctor3.put(COL_RATING, 4.8);
        doctor3.put(COL_EXPERIENCE, "25 Years");
        db.insert(TABLE_DOCTORS, null, doctor3);

// Add Radiology doctor
        ContentValues doctor4 = new ContentValues();
        doctor4.put(COL_DOCTOR_NAME, "Dr. Emily Wilson");
        doctor4.put(COL_SPECIALITY, "Radiology");
        doctor4.put(COL_RATING, 4.3);
        doctor4.put(COL_EXPERIENCE, "12 Years");
        db.insert(TABLE_DOCTORS, null, doctor4);

// Add Cardiology doctor
        ContentValues doctor5 = new ContentValues();
        doctor5.put(COL_DOCTOR_NAME, "Dr. James Peterson");
        doctor5.put(COL_SPECIALITY, "Cardiology");
        doctor5.put(COL_RATING, 4.7);
        doctor5.put(COL_EXPERIENCE, "18 Years");
        db.insert(TABLE_DOCTORS, null, doctor5);

// Add Pediatrician
        ContentValues doctor6 = new ContentValues();
        doctor6.put(COL_DOCTOR_NAME, "Dr. Lisa Wong");
        doctor6.put(COL_SPECIALITY, "Pediatrics");
        doctor6.put(COL_RATING, 4.9);
        doctor6.put(COL_EXPERIENCE, "10 Years");
        db.insert(TABLE_DOCTORS, null, doctor6);

        ContentValues doctor7 = new ContentValues();
        doctor7.put(COL_DOCTOR_NAME, "Dr. Philip Stieg");
        doctor7.put(COL_SPECIALITY, "Neurology");
        doctor7.put(COL_RATING, 4.2f);
        doctor7.put(COL_EXPERIENCE, "7 Years");
        db.insert(TABLE_DOCTORS, null, doctor7);
        Log.d("DatabaseHelper", "Sample doctors inserted.");
    }
    // Add to DatabaseHelper class
    public boolean bookAppointment(String doctorName, String speciality, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DOCTOR_NAME, doctorName);
        values.put(COL_SPECIALITY, speciality);
        values.put("appointment_date", date);
        values.put("appointment_time", time);
        long result = db.insert("appointments", null, values);
        return result != -1;
    }
    public boolean isSlotAvailable(String doctorName, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM appointments WHERE doctor_name = ? AND appointment_date = ? AND appointment_time = ?";
        Cursor cursor = db.rawQuery(query, new String[]{doctorName, date, time});

        boolean available = !cursor.moveToFirst();  // true if slot not found, i.e., available
        cursor.close();
        return available;
    }

}
