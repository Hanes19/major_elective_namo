package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ClientProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private AppCompatButton btnEdit, btnLogout;
    private TextView tvName, tvEmail;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_profile_screen);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadUserProfile();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        // Make sure you have a Logout button in your XML with id: btnLogout
        // If not, add one.
        btnLogout = findViewById(R.id.btnLogout);

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "No Email Found");

        if (tvEmail != null) tvEmail.setText(email);

        String name = dbHelper.getUsername(email);
        if (tvName != null) tvName.setText(name);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v ->
                Toast.makeText(this, "Edit feature coming soon", Toast.LENGTH_SHORT).show()
        );

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                // 1. Clear Session
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                // 2. Redirect to Login
                Intent intent = new Intent(ClientProfileActivity.this, f_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                startActivity(intent);
                finish();
            });
        }
    }
}