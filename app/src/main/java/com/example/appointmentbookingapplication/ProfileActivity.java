package com.example.appointmentbookingapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    static class Doctor {
        String name, speciality, address, experience, bio, website, phone;
        float rating;
        int imageResId;

        Doctor(String name, String speciality, String address, String experience, String bio, String website, String phone, float rating, int imageResId) {
            this.name = name;
            this.speciality = speciality;
            this.address = address;
            this.experience = experience;
            this.bio = bio;
            this.website = website;
            this.phone = phone;
            this.rating = rating;
            this.imageResId = imageResId;
        }
    }

    private HashMap<String, Doctor> getDoctorData() {
        HashMap<String, Doctor> doctors = new HashMap<>();
//
        doctors.put("Dr. Sarah Johnson", new Doctor(
                "Dr. Sarah Johnson", "Orthopedics",
                "12 SkinCare Ave, LA, California 90001", "10 years",
                " Experienced in complex joint replacements and spine surgeries with an emphasis on rapid recovery techniques.",
                "https://www.kiranhospital.com/", "1234560001", 4.5f, R.drawable.doctor1));

        doctors.put("Dr. Michael Roberts", new Doctor(
                "Dr. Michael Roberts", "Dentistry",
                "89 FitLife Rd, Austin, Texas 73301", "12 years",
                "Renowned cosmetic dentist with decades of experience in smile makeovers and celebrity dental care.",
                "https://www.kiranhospital.com/", "1234560002", 4.8f, R.drawable.doctor2));

        doctors.put("Dr. David Chen", new Doctor(
                "Dr. David Chen", "Orthopedics",
                "321 BoneCare Blvd, Seattle, Washington 98101", "15 years",
                "Experienced in complex joint replacements and spine surgeries with an emphasis on rapid recovery techniques.",
                "https://www.kiranhospital.com/", "1234560003", 4.6f, R.drawable.doctor3));

        doctors.put("Dr. Emily Wilson", new Doctor(
                "Dr. Chandler Bing", "Radiology",
                "200 MindHeal St, Denver, Colorado 80201", "11 years",
                "Senior radiologist specializing in cross-sectional imaging with a focus on precision diagnostics.",
                "https://www.kiranhospital.com/", "1234560004", 4.9f, R.drawable.doctor4));

        doctors.put("Dr. James Peterson", new Doctor(
                "Dr. James Peterson", "Cardiology",
                "154 KidCare Lane, Orlando, Florida 32801", "9 years",
                "A board-certified cardiologist specializing in heart conditions like coronary artery disease and arrhythmias. Known for patient-centered care.",
                "https://www.kiranhospital.com/", "1234560005", 4.7f, R.drawable.doctor5));

        doctors.put("Dr. Lisa Wong", new Doctor(
                "Dr. Lisa Wong", "Pediatrics",
                "77 Wellness Way, Sedona, Arizona 86336", "13 years",
                "Loves working with children and known for his fun approach to pediatric care, ensuring kids are comfortable during visits..",
                "https://www.kiranhospital.com/", "1234560006", 4.4f, R.drawable.doctor6));

        doctors.put("Dr. Philip Stieg", new Doctor(
                "Dr. Philip Stieg", "Neurology",
                "8502 Preston Rd, Inglewood, Maine 98380", "15 years",
                "Expert in stroke and epilepsy care, known for compassionate treatment and neurological awareness initiatives.",
                "https://www.kiranhospital.com/", "1234567890", 4.8f, R.drawable.doctor7));

        return doctors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        // Get doctor name from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("doctor_name");

        HashMap<String, Doctor> doctors = getDoctorData();
        Doctor doctor = doctors.get(name);

        if (doctor == null) return;

        // Initialize views
        ImageView doctorImage = findViewById(R.id.doctorImage);
        TextView doctorName = findViewById(R.id.doctorName);
        TextView doctorSpeciality = findViewById(R.id.doctorSpeciality);
        TextView doctorAddress = findViewById(R.id.doctorAddress);
        TextView doctorStats = findViewById(R.id.doctorStats);
        TextView doctorBio = findViewById(R.id.doctorBio);

        ImageButton btnWebsite = findViewById(R.id.btnWebsite);
        ImageButton btnMessage = findViewById(R.id.btnMessage);
        ImageButton btnCall = findViewById(R.id.btnCall);
        ImageButton btnDirection = findViewById(R.id.btnDirection);
        ImageButton btnShare = findViewById(R.id.btnShare);
        Button btnMakeAppointment = findViewById(R.id.btnMakeAppointment);

        // Set values
        doctorImage.setImageResource(doctor.imageResId);
        doctorName.setText(doctor.name);
        doctorSpeciality.setText(doctor.speciality);
        doctorAddress.setText(doctor.address);
        doctorStats.setText(String.format("Patients | Experience | Rating\n500+ | %s | %.1f", doctor.experience, doctor.rating));
        doctorBio.setText(doctor.bio);

        // Action buttons
        btnWebsite.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doctor.website));
            startActivity(browserIntent);
        });

        btnMessage.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + doctor.phone));
            startActivity(smsIntent);
        });

        btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + doctor.phone));
            startActivity(callIntent);
        });

        btnDirection.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(doctor.address));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out " + doctor.name + ", " + doctor.speciality + "\nAddress: " + doctor.address);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        btnMakeAppointment.setOnClickListener(v -> {
            Intent appointmentIntent = new Intent(this, BookAppointmentActivity.class);
            appointmentIntent.putExtra("doctor_name", doctor.name);
            appointmentIntent.putExtra("speciality", doctor.speciality);
            startActivity(appointmentIntent);
        });
    }
}
