package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private ImageButton btnTogglePass, btnToggleConfirmPass, btnBack;
    private AppCompatButton btnSignUp;
    private TextView tvSignIn;

    private DatabaseHelper dbHelper; // Database Helper
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnTogglePass = findViewById(R.id.btnTogglePass);
        btnToggleConfirmPass = findViewById(R.id.btnToggleConfirmPass);
        btnBack = findViewById(R.id.btnBack);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnTogglePass.setOnClickListener(v -> {
            isPasswordVisible = toggleVisibility(etPassword, btnTogglePass, isPasswordVisible);
        });

        btnToggleConfirmPass.setOnClickListener(v -> {
            isConfirmPasswordVisible = toggleVisibility(etConfirmPassword, btnToggleConfirmPass, isConfirmPasswordVisible);
        });

        btnSignUp.setOnClickListener(v -> performRegistration());

        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean toggleVisibility(EditText editText, ImageButton toggleButton, boolean isCurrentlyVisible) {
        if (isCurrentlyVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setAlpha(0.5f);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setAlpha(1.0f);
        }
        editText.setSelection(editText.getText().length());
        return !isCurrentlyVisible;
    }

    private void performRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            etEmail.setError("Enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Insert into Database
        boolean isInserted = dbHelper.registerUser(username, email, password);

        if (isInserted) {
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
            // Go to Login
            Intent intent = new Intent(RegisterActivity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration Failed. Email might be taken.", Toast.LENGTH_SHORT).show();
        }
    }
}