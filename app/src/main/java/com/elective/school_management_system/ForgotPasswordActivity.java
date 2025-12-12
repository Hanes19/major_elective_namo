package com.elective.school_management_system;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private AppCompatButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fogot_password_screen); // Ensure this matches your XML filename

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnGoBack = findViewById(R.id.btnGoBack);
    }

    private void setupListeners() {
        // Both buttons just close this screen, returning the user to the previous one
        btnBack.setOnClickListener(v -> finish());

        btnGoBack.setOnClickListener(v -> finish());
    }
}