package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class f_login extends AppCompatActivity {

    // 1. Declare UI Components
    private EditText etEmail, etPassword;
    private ImageButton btnTogglePass, btnBack;
    private AppCompatButton btnLogin;
    private TextView tvForgotPass, tvSignUp;

    // State variable for password visibility
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML filename (login_screen.xml)
        setContentView(R.layout.login_screen);

        // 2. Initialize Views
        initViews();

        // 3. Set up Click Listeners
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnTogglePass = findViewById(R.id.btnTogglePass);
        btnBack = findViewById(R.id.btnBack);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        tvSignUp = findViewById(R.id.tvSignUp);
    }

    private void setupListeners() {
        // --- Back Button Functionality ---
        btnBack.setOnClickListener(v -> {
            // Closes the current activity and returns to the previous one
            finish();
        });

        // --- Password Visibility Toggle ---
        btnTogglePass.setOnClickListener(v -> togglePasswordVisibility());

        // --- Login Button Logic ---
        btnLogin.setOnClickListener(v -> performLogin());

        // --- Forgot Password Navigation (UPDATED) ---
        tvForgotPass.setOnClickListener(v -> {
            // Navigate to the newly created ForgotPasswordActivity
            Intent intent = new Intent(f_login.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // --- Sign Up Navigation (UPDATED) ---
        tvSignUp.setOnClickListener(v -> {
            // Navigate to the new RegisterActivity
            Intent intent = new Intent(f_login.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide Password
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // Update icon to "eye with slash" or closed eye if you have that asset
            // btnTogglePass.setImageResource(R.drawable.pswrd_view_txtbox_ic);
            // For now, we just tint it or keep same icon to indicate state change visually
            btnTogglePass.setAlpha(0.5f); // Dim the icon to show hidden
        } else {
            // Show Password
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePass.setAlpha(1.0f); // Brighten icon to show visible
        }

        // Move cursor to the end of the text
        etPassword.setSelection(etPassword.getText().length());

        isPasswordVisible = !isPasswordVisible;
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic Validation
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Mock Login Success
        // In a real app, you would verify credentials with Firebase or a Backend API here
        if (email.contains("@") && password.length() > 4) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            // Navigate to Dashboard
            // Intent intent = new Intent(f_login.this, MainActivity.class); // or DashboardActivity
            // startActivity(intent);
            // finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}