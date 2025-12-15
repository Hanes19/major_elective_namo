package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentEditProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etName, etCourse, etYear, etSection, etEmail, etPhone;
    private Button btnSaveChanges;
    // REMOVED: btnResetPass, btnForgotPass (They are not in the layout)

    private DatabaseHelper dbHelper;
    private String currentEmail;
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_edit_profile);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        etName = findViewById(R.id.etName);
        etCourse = findViewById(R.id.etCourse);
        etYear = findViewById(R.id.etYear);
        etSection = findViewById(R.id.etSection);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (currentEmail.isEmpty()) {
            finish();
            return;
        }

        String name = dbHelper.getUsername(currentEmail);
        currentUserId = dbHelper.getUserId(currentEmail);

        etName.setText(name);
        etEmail.setText(currentEmail);

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

        // REMOVED: Listeners for missing buttons (btnResetPass, btnForgotPass) caused the crash

        // Save Changes Button
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

            if (!newEmail.equals(currentEmail) && dbHelper.checkEmailExists(newEmail)) {
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean userUpdated = dbHelper.updateUser(currentEmail, newName, newEmail);
            boolean profileUpdated = false;

            if (currentUserId != -1) {
                profileUpdated = dbHelper.saveUserProfile(currentUserId, course, year, section, phone);
            }

            if (userUpdated || profileUpdated) {
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