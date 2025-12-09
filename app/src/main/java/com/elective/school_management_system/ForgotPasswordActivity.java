package com.elective.school_management_system;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.Toast;

// Note: The original f_login.java referenced a "ForgotPasswordActivity.class"
public class ForgotPasswordActivity extends AppCompatActivity {

    // 1. Declare UI Components (matching IDs from fogot_password_screen.xml)
    private ImageButton btnBack;
    private AppCompatButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML filename
        setContentView(R.layout.fogot_password_screen);

        // 2. Initialize Views
        initViews();

        // 3. Set up Click Listeners
        setupListeners();
    }

    private void initViews() {
        // The back arrow at the top
        btnBack = findViewById(R.id.btnBack);
        // The large button at the bottom
        btnGoBack = findViewById(R.id.btnGoBack);
    }

    private void setupListeners() {
        // Define a listener for returning to the previous screen (f_login)
        View.OnClickListener goBackListener = v -> {
            // Closes the current activity and returns to the previous one
            finish();
        };

        // --- Back Arrow Functionality ---
        btnBack.setOnClickListener(goBackListener);

        // --- Go Back Button Logic ---
        btnGoBack.setOnClickListener(goBackListener);
    }
}