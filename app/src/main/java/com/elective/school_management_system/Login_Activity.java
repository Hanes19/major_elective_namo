package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class Login_Activity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ImageButton btnTogglePass, btnBack;
    private AppCompatButton btnLogin;
    private TextView tvForgotPass, tvSignUp;
    private DatabaseHelper dbHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. CHECK SESSION BEFORE LOADING VIEW
        if (checkSession()) {
            return;
        }

        setContentView(R.layout.l_login_screen);
        dbHelper = new DatabaseHelper(this);
        initViews();
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
        btnBack.setOnClickListener(v -> finish());

        btnTogglePass.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnTogglePass.setAlpha(0.5f);
            } else {
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnTogglePass.setAlpha(1.0f);
            }
            etPassword.setSelection(etPassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        tvForgotPass.setOnClickListener(v -> startActivity(new Intent(Login_Activity.this, ForgotPasswordActivity.class)));
        tvSignUp.setOnClickListener(v -> startActivity(new Intent(Login_Activity.this, RegisterActivity.class)));
        btnLogin.setOnClickListener(v -> performLogin());
    }

    // --- Session Check Function ---
    private boolean checkSession() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String role = prefs.getString("role", "client");

        if (isLoggedIn) {
            Intent intent;
            if (role.equals("admin")) {
                intent = new Intent(Login_Activity.this, AdminDashboardActivity.class);
                Toast.makeText(this, "Welcome back, Admin!", Toast.LENGTH_SHORT).show();
            } else if (role.equals("teacher")) {
                intent = new Intent(Login_Activity.this, TeacherDashboardActivity.class);
                Toast.makeText(this, "Welcome back, Teacher!", Toast.LENGTH_SHORT).show();
            } else {
                // CHANGED: Now redirects to the new Fragment Container Activity
                intent = new Intent(Login_Activity.this, StudentMainActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 2. CHECK FOR ADMIN CREDENTIALS (HARDCODED FALLBACK)
        if (email.equals("admin") && password.equals("admin123")) {
            editor.putString("email", email);
            editor.putString("role", "admin");
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Toast.makeText(this, "Login Successful (Admin)", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login_Activity.this, AdminDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        // 3. DATABASE CHECK
        else if (dbHelper.checkUser(email, password)) {
            // Retrieve role from Database
            String role = dbHelper.getUserRole(email);

            editor.putString("email", email);
            editor.putString("role", role);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent intent;
            if (role.equalsIgnoreCase("teacher")) {
                Toast.makeText(this, "Welcome, Teacher!", Toast.LENGTH_SHORT).show();
                intent = new Intent(Login_Activity.this, TeacherDashboardActivity.class);
            } else if (role.equalsIgnoreCase("admin")) {
                Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
                intent = new Intent(Login_Activity.this, AdminMainActivity.class);
            } else {
                // Default to Student/Client
                // CHANGED: Now redirects to the new Fragment Container Activity
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                intent = new Intent(Login_Activity.this, StudentMainActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}