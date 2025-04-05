package com.example.appointmentbookingapplication;

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
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
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
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform logout
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        preferences.edit().clear().apply();

                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        specialties.add("Pediatrician");

        SpecialtiesAdapter specialtiesAdapter = new SpecialtiesAdapter(specialties);
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
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("doctor_name"));
            String speciality = cursor.getString(cursor.getColumnIndexOrThrow("speciality"));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
            String experience = cursor.getString(cursor.getColumnIndexOrThrow("experience"));

            int[] doctorImages = { R.drawable.doctor1, R.drawable.doctor2 };
            int imageResId = doctorImages[doctorList.size() % doctorImages.length];

            doctorList.add(new Doctor(name, speciality, rating, experience, imageResId));
        }
        cursor.close();

        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(doctorList);
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        doctorsRecyclerView.setAdapter(doctorsAdapter);
    }


    private void setupBottomNavigation() {
        findViewById(R.id.nav_explorer).setOnClickListener(v -> {
            // Handle explorer click
        });

        findViewById(R.id.nav_waitlist).setOnClickListener(v -> {
            // Handle waitlist click
        });

        findViewById(R.id.nav_settings).setOnClickListener(v -> {
            // Handle settings click
        });

        findViewById(R.id.nav_account).setOnClickListener(v -> {
            // Navigate to profile activity
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }
}
