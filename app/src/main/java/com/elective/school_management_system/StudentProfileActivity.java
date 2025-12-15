package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentProfileActivity extends AppCompatActivity {

    private ImageButton btnBack, btnEdit; // Changed from AppCompatButton/ImageView to ImageButton
    private TextView btnLogout;           // Changed from AppCompatButton to TextView (it is a TextView in XML)
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
        // Matches XML types
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
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> logout());
        }

        // EDIT BUTTON LOGIC
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentEditProfileActivity.class);
                startActivity(intent);
            });
        }
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