package com.example.appointmentbookingapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        // Get doctor data from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("doctor_name");
        String speciality = intent.getStringExtra("speciality");
        float rating = intent.getFloatExtra("rating", 0);
        String experience = intent.getStringExtra("experience");
        int imageResId = intent.getIntExtra("image_res_id", R.drawable.ic_default_doctor);

        // Initialize views
        ImageView doctorImage = findViewById(R.id.doctorImage);
        TextView doctorName = findViewById(R.id.doctorName);
        TextView doctorSpeciality = findViewById(R.id.doctorSpeciality);
        TextView doctorAddress = findViewById(R.id.doctorAddress);
        TextView doctorStats = findViewById(R.id.doctorStats);
        TextView doctorBio = findViewById(R.id.doctorBio);
        Button btnWebsite = findViewById(R.id.btnWebsite);
        Button btnMessage = findViewById(R.id.btnMessage);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnDirection = findViewById(R.id.btnDirection);
        Button btnShare = findViewById(R.id.btnShare);
        Button btnMakeAppointment = findViewById(R.id.btnMakeAppointment);

        // Set doctor data
        doctorImage.setImageResource(imageResId);
        doctorName.setText(name);
        doctorSpeciality.setText(speciality);
        doctorAddress.setText("8502 Preston Rd, Inglewood, Maine 98380");
        doctorStats.setText(String.format("Patients | Experience | Rating\n500+ | %s | %.1f", experience, rating));
        doctorBio.setText("A board-certified with over 15 years of experience, specializing in heart conditions such as coronary artery disease and arrhythmias. Known for patient-centered care and a commitment to the latest medical advancements");

        // Set click listeners for action buttons
        btnWebsite.setOnClickListener(v -> {
            // Open website
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"));
            startActivity(browserIntent);
        });

        btnMessage.setOnClickListener(v -> {
            // Open messaging app
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
            startActivity(smsIntent);
        });

        btnCall.setOnClickListener(v -> {
            // Initiate phone call
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567890"));
            startActivity(callIntent);
        });

        btnDirection.setOnClickListener(v -> {
            // Open map with directions
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=8502+Preston+Rd,+Inglewood,+Maine+98380");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        btnShare.setOnClickListener(v -> {
            // Share doctor profile
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out Dr. " + name + ", " + speciality + "\nAddress: 8502 Preston Rd, Inglewood, Maine 98380");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        btnMakeAppointment.setOnClickListener(v -> {
            // Open appointment booking activity
            Intent appointmentIntent = new Intent(this, BookAppointmentActivity.class);
            appointmentIntent.putExtra("doctor_name", name);
            appointmentIntent.putExtra("speciality", speciality);
            startActivity(appointmentIntent);
        });
    }
}