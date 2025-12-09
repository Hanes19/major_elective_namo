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

public class RegisterActivity extends AppCompatActivity {

    // 1. Declare UI Components (matching IDs from register_screen.xml)
    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private ImageButton btnTogglePass, btnToggleConfirmPass, btnBack;
    private AppCompatButton btnSignUp;
    private TextView tvSignIn;

    // State variables for password visibility
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML filename
        setContentView(R.layout.register_screen);

        // 2. Initialize Views
        initViews();

        // 3. Set up Click Listeners
        setupListeners();
    }

    private void initViews() {
        // Input Fields
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Buttons
        btnTogglePass = findViewById(R.id.btnTogglePass);
        btnToggleConfirmPass = findViewById(R.id.btnToggleConfirmPass);
        btnBack = findViewById(R.id.btnBack);
        btnSignUp = findViewById(R.id.btnSignUp);

        // TextView Link
        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void setupListeners() {
        // --- Back Button Functionality ---
        btnBack.setOnClickListener(v -> {
            // Closes the current activity and returns to the previous one (f_login)
            finish();
        });

        // --- Password Visibility Toggle (Password) ---
        btnTogglePass.setOnClickListener(v -> {
            isPasswordVisible = toggleVisibility(etPassword, btnTogglePass, isPasswordVisible);
        });

        // --- Password Visibility Toggle (Confirm Password) ---
        btnToggleConfirmPass.setOnClickListener(v -> {
            isConfirmPasswordVisible = toggleVisibility(etConfirmPassword, btnToggleConfirmPass, isConfirmPasswordVisible);
        });

        // --- Sign Up Button Logic ---
        btnSignUp.setOnClickListener(v -> performRegistration());

        // --- Sign In Navigation ---
        tvSignIn.setOnClickListener(v -> {
            // Navigate back to the Login Activity (f_login)
            // You can use 'finish()' to go back to the previous activity or use an explicit Intent
            Intent intent = new Intent(RegisterActivity.this, f_login.class);
            startActivity(intent);
            finish(); // Close RegisterActivity
        });
    }

// --------------------------------------------------------------------------------

    /**
     * Reusable method to toggle the visibility of an EditText field and update the button's visual state.
     */
    private boolean toggleVisibility(EditText editText, ImageButton toggleButton, boolean isCurrentlyVisible) {
        if (isCurrentlyVisible) {
            // Hide Password
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setAlpha(0.5f); // Dim the icon to show hidden
        } else {
            // Show Password
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setAlpha(1.0f); // Brighten icon to show visible
        }

        // Move cursor to the end of the text
        editText.setSelection(editText.getText().length());

        return !isCurrentlyVisible; // Return the new state
    }

// --------------------------------------------------------------------------------

    /**
     * Performs basic input validation and mock registration logic.
     */
    private void performRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 1. Basic Validation (Empty Fields)
        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

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

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirm Password is required");
            etConfirmPassword.requestFocus();
            return;
        }

        // 2. Format Validation
        if (!email.contains("@")) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (password.length() < 6) { // Common minimum password length
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // 3. Password Match Validation
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            // Clear both password fields for security/user experience
            etPassword.setText("");
            etConfirmPassword.setText("");
            isPasswordVisible = isConfirmPasswordVisible = false; // Reset visibility state
            return;
        }

        // 4. Mock Registration Success
        // In a real application, you would send data to Firebase or a Backend API here.
        Toast.makeText(this, "Registration Successful for: " + username, Toast.LENGTH_LONG).show();

        // Navigate to the next screen (e.g., Dashboard or back to Login)
        // Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        // startActivity(intent);
        // finish();
    }
}