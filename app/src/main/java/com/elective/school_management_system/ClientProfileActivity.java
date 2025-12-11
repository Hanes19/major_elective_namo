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
    private AppCompatButton btnEdit, btnLogout; // Assumes you might add a logout button later
    private TextView tvName, tvEmail; // You need to add IDs to your XML textviews for this to work
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

        // IMPORTANT: In your XML (p_profile_screen.xml), give IDs to the TextViews
        // that hold the Name and Email. For example: android:id="@+id/tvProfileName"
        // Since I don't see the XML IDs, I am assuming standard names:
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "No Email Found");

        if (tvEmail != null) tvEmail.setText(email);

        // Fetch Name from DB
        String name = dbHelper.getUsername(email);
        if (tvName != null) tvName.setText(name);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Edit feature coming soon", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ClientProfileActivity.this, ClientEditProfileActivity.class);
            // startActivity(intent);
        });

        // If you have a logout button:
        /*
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, f_login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        */
    }
}