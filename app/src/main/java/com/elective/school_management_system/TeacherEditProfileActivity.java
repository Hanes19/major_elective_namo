package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class TeacherEditProfileActivity extends AppCompatActivity {

    private EditText etName, etDepartment, etFacultyId, etDesignation, etEmail, etPhone;
    private AppCompatButton btnSaveChanges;
    private ImageButton btnBack;

    private DatabaseHelper dbHelper;
    private String userEmail;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_edit_profile);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", "");
        userId = dbHelper.getUserId(userEmail);

        if (userId == -1) {
            Toast.makeText(this, "Session Error. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadCurrentData();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etDepartment = findViewById(R.id.etDepartment);
        etFacultyId = findViewById(R.id.etFacultyId);
        etDesignation = findViewById(R.id.etDesignation);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadCurrentData() {
        // Load Username/Email from User Table
        String currentName = dbHelper.getUsername(userEmail);
        etName.setText(currentName);
        etEmail.setText(userEmail);

        // Load Profile Data
        Cursor cursor = dbHelper.getUserProfile(userId);
        if (cursor != null && cursor.moveToFirst()) {
            // MAPPING: Course -> Department, Year -> Designation, Section -> Faculty ID
            String department = cursor.getString(cursor.getColumnIndexOrThrow("course"));
            String designation = cursor.getString(cursor.getColumnIndexOrThrow("year_level"));
            String facultyId = cursor.getString(cursor.getColumnIndexOrThrow("section"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

            etDepartment.setText(department);
            etDesignation.setText(designation);
            etFacultyId.setText(facultyId);
            etPhone.setText(phone);

            cursor.close();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String newName = etName.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String designation = etDesignation.getText().toString().trim();
        String facultyId = etFacultyId.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Name and Email are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Update User Table (Name/Email)
        boolean userUpdated = dbHelper.updateUser(userEmail, newName, newEmail);

        // 2. Update Profile Table
        // MAPPING: Course -> Department, Year -> Designation, Section -> Faculty ID
        boolean profileUpdated = dbHelper.saveUserProfile(userId, department, designation, facultyId, phone);

        if (userUpdated || profileUpdated) {
            Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();

            // Update Session if email changed
            if (!userEmail.equals(newEmail)) {
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                prefs.edit().putString("email", newEmail).apply();
            }

            finish();
        } else {
            Toast.makeText(this, "Update Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}