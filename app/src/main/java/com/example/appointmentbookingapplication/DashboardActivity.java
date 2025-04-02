package com.example.appointmentbookingapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

        setupSpecialtiesRecyclerView();
        setupDoctorsRecyclerView();

        // Set welcome message with user's full name
        String fullName = databaseHelper.getUserFullName(email);
        welcomeTextView.setText(String.format("Hi, %s\nHow Are You Today?", fullName));

        // Setup specialties recycler view
        setupSpecialtiesRecyclerView();

        // Setup doctors recycler view
        setupDoctorsRecyclerView();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void setupSpecialtiesRecyclerView() {
        List<String> specialties = new ArrayList<>();
        specialties.add("Cardiology");
        specialties.add("Dentistry");
        specialties.add("Neurology");
        specialties.add("Orthopedics");
        specialties.add("Radiol");

        SpecialtiesAdapter specialtiesAdapter = new SpecialtiesAdapter(specialties);
        specialtiesRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        specialtiesRecyclerView.setAdapter(specialtiesAdapter);
    }

    private void setupDoctorsRecyclerView() {
        List<Doctor> doctors = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllDoctors();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("doctor_name"));
                String speciality = cursor.getString(cursor.getColumnIndexOrThrow("speciality"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                String experience = cursor.getString(cursor.getColumnIndexOrThrow("experience"));
                doctors.add(new Doctor(name, speciality, rating, experience));
            } while (cursor.moveToNext());
        }
        cursor.close();

        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(doctors);
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
            // Handle account click
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }
}