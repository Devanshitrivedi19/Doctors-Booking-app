package com.example.appointmentbookingapplication;

import static com.example.appointmentbookingapplication.DatabaseHelper.COL_DOCTOR_NAME;
import static com.example.appointmentbookingapplication.DatabaseHelper.COL_EXPERIENCE;
import static com.example.appointmentbookingapplication.DatabaseHelper.COL_RATING;
import static com.example.appointmentbookingapplication.DatabaseHelper.COL_SPECIALITY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements
        SpecialtiesAdapter.OnSpecialtyClickListener {
    private TextView welcomeTextView;
    private RecyclerView specialtiesRecyclerView, doctorsRecyclerView;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        String email = getIntent().getStringExtra("email");
        databaseHelper = new DatabaseHelper(this);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        specialtiesRecyclerView = findViewById(R.id.specialtiesRecyclerView);
        doctorsRecyclerView = findViewById(R.id.doctorsRecyclerView);
        Button logoutButton = findViewById(R.id.logoutButton);
// Set welcome message with user's full name
        String fullName = databaseHelper.getUserFullName(email);
        welcomeTextView.setText(String.format("Hi, %s\nHow Are You Today?", fullName));
// Setup RecyclerViews
        setupSpecialtiesRecyclerView();
        setupDoctorsRecyclerView();
// Setup bottom navigation
        setupBottomNavigation();
// Setup "See all" click listener
        findViewById(R.id.seeAllDoctors).setOnClickListener(v -> {
            setupDoctorsRecyclerView(); // Reset to show all doctors
// Clear specialty selection
            if (specialtiesRecyclerView.getAdapter() instanceof SpecialtiesAdapter) {
                ((SpecialtiesAdapter) specialtiesRecyclerView.getAdapter()).clearSelection();
            }
        });
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
// Perform logout
                        SharedPreferences preferences = getSharedPreferences("MyPrefs",
                                MODE_PRIVATE);
                        preferences.edit().clear().apply();
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
    private void setupSpecialtiesRecyclerView() {
        List<String> specialties = new ArrayList<>();
        specialties.add("Cardiology");
        specialties.add("Dentistry");
        specialties.add("Neurology");
        specialties.add("Orthopedics");
        specialties.add("Radiology");
        specialties.add("Pediatrics");
// Pass 'this' as the click listener (DashboardActivity implements the interface)
        SpecialtiesAdapter specialtiesAdapter = new SpecialtiesAdapter(specialties, this);
        specialtiesRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        specialtiesRecyclerView.setAdapter(specialtiesAdapter);
    }
    private void setupDoctorsRecyclerView() {
        Cursor cursor = databaseHelper.getAllDoctors();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No doctors available", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Doctor> doctorList = new ArrayList<>();
        int[] doctorImages = {
                R.drawable.doctor1, R.drawable.doctor2, R.drawable.doctor3,
                R.drawable.doctor4, R.drawable.doctor5, R.drawable.doctor6,
                R.drawable.doctor7
        };

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("doctor_name"));
                String speciality = cursor.getString(cursor.getColumnIndexOrThrow("speciality"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                String experience = cursor.getString(cursor.getColumnIndexOrThrow("experience"));

                // Assign image based on hash of doctor name
                Map<String, Integer> nameToImageMap = new HashMap<>();
                nameToImageMap.put("Dr. Sarah Johnson", R.drawable.doctor1);
                nameToImageMap.put("Dr. Michael Roberts", R.drawable.doctor2);
                nameToImageMap.put("Dr. David Chen", R.drawable.doctor3);
                nameToImageMap.put("Dr. Emily Wilson", R.drawable.doctor4);
                nameToImageMap.put("Dr. James Peterson", R.drawable.doctor5);
                nameToImageMap.put("Dr. Lisa Wong", R.drawable.doctor6);
                nameToImageMap.put("Dr. Philip Stieg", R.drawable.doctor7);

// Add more mappings

                int imageResId =nameToImageMap.get(name);

                doctorList.add(new Doctor(name, speciality, rating, experience, imageResId));
            }
        } finally {
            cursor.close();
        }

        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(doctorList);
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        doctorsRecyclerView.setAdapter(doctorsAdapter);
    }

    @Override
    public void onSpecialtyClick(String specialty) {
        filterDoctorsBySpecialty(specialty);
    }

    private void filterDoctorsBySpecialty(String specialty) {
        Cursor cursor = databaseHelper.getDoctorsBySpecialty(specialty);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No doctors available in " + specialty,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        List<Doctor> doctorList = new ArrayList<>();
        int[] doctorImages = {R.drawable.doctor1, R.drawable.doctor2, R.drawable.doctor3,
                R.drawable.doctor4, R.drawable.doctor5, R.drawable.doctor6,
                R.drawable.doctor7};

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCTOR_NAME));
                String speciality = cursor.getString(cursor.getColumnIndexOrThrow(COL_SPECIALITY));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_RATING));
                String experience = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPERIENCE));

                // Consistent image assignment using name hash
                int imageIndex = Math.abs(name.hashCode()) % doctorImages.length;
                int imageResId = doctorImages[imageIndex];

                doctorList.add(new Doctor(name, speciality, rating, experience, imageResId));
            }
        } finally {
            cursor.close();
        }

        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(doctorList);
        doctorsRecyclerView.setAdapter(doctorsAdapter);
    }

    private void setupBottomNavigation() {
        findViewById(R.id.nav_explorer).setOnClickListener(v -> {
// Handle settings click
        });
        findViewById(R.id.nav_waitlist).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(DashboardActivity.this, WaitListActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(DashboardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace(); // Also view this in Logcat
            }
        });
        findViewById(R.id.nav_settings).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
        });

        findViewById(R.id.nav_account).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
        });
    }
}