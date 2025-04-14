package com.example.appointmentbookingapplication;


import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WaitListActivity extends AppCompatActivity {

    private RecyclerView waitlistRecyclerView;
    private WaitlistAdapter waitlistAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<Appointment> waitlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitlist);

        waitlistRecyclerView = findViewById(R.id.waitlistRecyclerView);
        databaseHelper = new DatabaseHelper(this);
        waitlist = new ArrayList<>();

        loadWaitlistData();

        waitlistAdapter = new WaitlistAdapter(waitlist);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        waitlistRecyclerView.setAdapter(waitlistAdapter);
    }

    private void loadWaitlistData() {
        Cursor cursor = databaseHelper.getAllWaitlistedAppointments();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String patientName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PATIENT_NAME));
                String doctorName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_NAME)); // Added
                String appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));
                String appointmentTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIME));

                waitlist.add(new Appointment(patientName, doctorName, appointmentDate, appointmentTime));
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No waitlisted appointments found.", Toast.LENGTH_SHORT).show();
        }
    }
}
