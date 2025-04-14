package com.example.appointmentbookingapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity {

    private EditText userNameEditText, userEmailEditText, userPhoneEditText;
    private TextView doctorNameTextView, dateTextView, timeTextView;
    private DatabaseHelper databaseHelper;
    private String doctorName, speciality, date = "", time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        databaseHelper = new DatabaseHelper(this);

        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        userPhoneEditText = findViewById(R.id.userPhoneEditText);
        doctorNameTextView = findViewById(R.id.doctorNameTextView);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        Button confirmButton = findViewById(R.id.confirmButton);

        doctorName = getIntent().getStringExtra("doctor_name");
        speciality = getIntent().getStringExtra("speciality");
        doctorNameTextView.setText(String.format("Dr. %s (%s)", doctorName, speciality));

        // Allow user to pick date
        dateTextView.setOnClickListener(v -> showDatePicker());

        // Allow user to pick time
        timeTextView.setOnClickListener(v -> showTimePicker());

        confirmButton.setOnClickListener(v -> {
            String patientName = userNameEditText.getText().toString().trim();
            String phone = userPhoneEditText.getText().toString().trim();
            String email = userEmailEditText.getText().toString().trim();

            if (patientName.isEmpty() || phone.isEmpty() || email.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all details including date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isSlotAvailable(doctorName, date, time)) {
                boolean success = databaseHelper.bookAppointment(
                        doctorName,
                        speciality,
                        date,
                        time
                );

                if (success) {
                    Toast.makeText(this, "Appointment Booked!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                addToWaitlist(patientName, phone, email);
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    calendar.set(year, month, dayOfMonth);
                    date = sdf.format(calendar.getTime());
                    dateTextView.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timeTextView.setText(time);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void addToWaitlist(String patientName, String phone, String email) {
        boolean added = databaseHelper.addToWaitlist(
                patientName,
                doctorName,
                phone,
                email,
                date,
                time
        );

        if (added) {
            Toast.makeText(this, "Slot Full! Added to Waitlist", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to Add to Waitlist", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSlotAvailable(String doctor, String date, String time) {
        return databaseHelper.isSlotAvailable(doctor, date, time);
    }
}
