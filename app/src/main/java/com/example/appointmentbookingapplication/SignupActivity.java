package com.example.appointmentbookingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText etFullName, etEmail, etAge, etPassword;
    RadioGroup rgGender;
    Spinner spStateDistrict;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        etFullName = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etAge = findViewById(R.id.et_age);
        rgGender = findViewById(R.id.rg_gender);
        spStateDistrict = findViewById(R.id.spinner_state_district);
        etPassword = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = selectedGenderId == -1 ? "" : ((RadioButton) findViewById(selectedGenderId)).getText().toString();
        String state = spStateDistrict.getSelectedItem().toString();

        if (fullName.isEmpty() || email.isEmpty() || ageStr.isEmpty() || password.isEmpty() || gender.isEmpty() || state.equals("Select State/District")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.registerUser(fullName, email, age, gender, state, password);

        if (success) {
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
        }
    }
}

