package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // Import Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class StudentProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private AppCompatButton btnEdit, btnLogout;
    private TextView tvName, tvEmail;
    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_profile);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadUserProfile();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(); // Refresh data when coming back
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (currentEmail.isEmpty()) {
            return;
        }

        if (tvEmail != null) tvEmail.setText(currentEmail);
        String name = dbHelper.getUsername(currentEmail);
        if (tvName != null) tvName.setText(name);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(v -> logout());

        // EDIT BUTTON LOGIC
        btnEdit.setOnClickListener(v -> {
            // Debug Toast: If you don't see this, the button isn't clicking.
            Toast.makeText(StudentProfileActivity.this, "Opening Edit Profile...", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(StudentProfileActivity.this, StudentEditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(StudentProfileActivity.this, Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}