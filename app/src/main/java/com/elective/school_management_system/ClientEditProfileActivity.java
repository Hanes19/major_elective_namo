package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ClientEditProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etName, etCourse, etYear, etSection, etEmail, etPhone;
    private Button btnSaveChanges;
    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_client);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        // Map the IDs from your XML file
        etName = findViewById(R.id.editTextText6);    // Name
        etCourse = findViewById(R.id.editTextText5);  // Course
        etYear = findViewById(R.id.editTextText9);    // Year
        etSection = findViewById(R.id.editTextText8); // Section
        etEmail = findViewById(R.id.editTextText4);   // Email
        etPhone = findViewById(R.id.editTextText3);   // Phone

        btnSaveChanges = findViewById(R.id.btnSaveChanges); // Ensure you added this ID in XML
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (currentEmail.isEmpty()) {
            Toast.makeText(this, "Session Error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load data from DB
        String name = dbHelper.getUsername(currentEmail);

        // Set text
        etName.setText(name);
        etEmail.setText(currentEmail);

        // Note: Course, Year, Section, Phone are not in your DatabaseHelper yet,
        // so they are just empty fields for now.
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSaveChanges.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and Email are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if email changed and if it's taken
            if (!newEmail.equals(currentEmail) && dbHelper.checkEmailExists(newEmail)) {
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update Database
            boolean success = dbHelper.updateUser(currentEmail, newName, newEmail);
            if (success) {
                // Update Session
                if (!currentEmail.equals(newEmail)) {
                    getSharedPreferences("UserSession", MODE_PRIVATE)
                            .edit().putString("email", newEmail).apply();
                }

                Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close this screen and go back
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}