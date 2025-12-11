package com.elective.school_management_system;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private AppCompatButton btnReset;
    private EditText etEmail, etNewPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fogot_password_screen);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        // Rename ID in XML or match here: assuming btnGoBack is the submit button
        btnReset = findViewById(R.id.btnGoBack);

        // Make sure these IDs exist in your XML
        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etPassword); // Using etPassword or create a new one
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            // If you don't have a new password field, you can generate a temp one,
            // but here we assume the user enters it.
            String newPass = (etNewPassword != null) ? etNewPassword.getText().toString().trim() : "123456";

            if(email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(dbHelper.checkEmailExists(email)) {
                boolean success = dbHelper.updatePassword(email, newPass);
                if(success) {
                    Toast.makeText(this, "Password Reset Successful! Please Login.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Database Error.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email not found registered.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}