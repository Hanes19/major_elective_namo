package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ClientProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private AppCompatButton btnEdit, btnLogout;
    private TextView tvName, tvEmail;
    private DatabaseHelper dbHelper;
    private String currentEmail;

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
        btnLogout = findViewById(R.id.btnLogout);
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (tvEmail != null) tvEmail.setText(currentEmail);

        String name = dbHelper.getUsername(currentEmail);
        if (tvName != null) tvName.setText(name);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(v -> logout());
        btnEdit.setOnClickListener(v -> showEditProfileDialog());
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final EditText etName = new EditText(this);
        etName.setText(tvName.getText().toString());
        etName.setHint("Full Name");
        layout.addView(etName);

        final EditText etNewEmail = new EditText(this);
        etNewEmail.setText(currentEmail);
        etNewEmail.setHint("Email Address");
        layout.addView(etNewEmail);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            String newEmail = etNewEmail.getText().toString().trim();

            if (!newName.isEmpty() && !newEmail.isEmpty()) {
                boolean success = dbHelper.updateUser(currentEmail, newName, newEmail);
                if (success) {
                    // Update Session if email changed
                    if(!currentEmail.equals(newEmail)) {
                        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                        prefs.edit().putString("email", newEmail).apply();
                        currentEmail = newEmail;
                    }
                    loadUserProfile();
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ClientProfileActivity.this, f_login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}