package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.database.Cursor;
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
    private int currentUserId = -1;

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

        etName = findViewById(R.id.editTextText6);    // Name
        etCourse = findViewById(R.id.editTextText5);  // Course
        etYear = findViewById(R.id.editTextText9);    // Year
        etSection = findViewById(R.id.editTextText8); // Section
        etEmail = findViewById(R.id.editTextText4);   // Email
        etPhone = findViewById(R.id.editTextText3);   // Phone

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (currentEmail.isEmpty()) {
            Toast.makeText(this, "Session Error. Please Login Again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 1. Get Main User Data
        String name = dbHelper.getUsername(currentEmail);
        currentUserId = dbHelper.getUserId(currentEmail);

        etName.setText(name);
        etEmail.setText(currentEmail);

        // 2. Get Extended Profile Data from new Table
        if (currentUserId != -1) {
            Cursor cursor = dbHelper.getUserProfile(currentUserId);
            if (cursor != null && cursor.moveToFirst()) {
                etCourse.setText(cursor.getString(cursor.getColumnIndexOrThrow("course")));
                etYear.setText(cursor.getString(cursor.getColumnIndexOrThrow("year_level")));
                etSection.setText(cursor.getString(cursor.getColumnIndexOrThrow("section")));
                etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                cursor.close();
            }
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSaveChanges.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String course = etCourse.getText().toString().trim();
            String year = etYear.getText().toString().trim();
            String section = etSection.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and Email are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if email changed
            if (!newEmail.equals(currentEmail) && dbHelper.checkEmailExists(newEmail)) {
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Update Basic Info (Users Table)
            boolean userUpdated = dbHelper.updateUser(currentEmail, newName, newEmail);

            // 2. Update Extended Info (Profiles Table)
            // If userUpdated failed, we shouldn't proceed, but for safety we check userId
            boolean profileUpdated = false;
            if (currentUserId != -1) {
                profileUpdated = dbHelper.saveUserProfile(currentUserId, course, year, section, phone);
            }

            if (userUpdated || profileUpdated) {
                // Update Session if email changed
                if (!currentEmail.equals(newEmail)) {
                    getSharedPreferences("UserSession", MODE_PRIVATE)
                            .edit().putString("email", newEmail).apply();
                }

                Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}