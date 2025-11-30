package com.elective.school_management_system;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class f_login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ImageView btnTogglePass, btnBack;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPass;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        // 1. Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnTogglePass = findViewById(R.id.btnTogglePass);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        // 2. Password Visibility Toggle Logic
        btnTogglePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide Password
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    // Use standard eye icon (ensure you have this drawable)
                    btnTogglePass.setImageResource(R.drawable.ic_visibility);
                } else {
                    // Show Password
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    // Ideally switch to an 'eye-off' icon here if you have one
                    btnTogglePass.setImageResource(R.drawable.ic_visibility);
                }
                // Move cursor to end of text
                etPassword.setSelection(etPassword.getText().length());
                isPasswordVisible = !isPasswordVisible;
            }
        });

        // 3. Login Button Logic
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(f_login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform your authentication logic here
                    Toast.makeText(f_login.this, "Logging in...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 4. Navigation Logic
        tvSignUp.setOnClickListener(v ->
                Toast.makeText(f_login.this, "Go to Sign Up", Toast.LENGTH_SHORT).show()
        );

        btnBack.setOnClickListener(v -> finish());
    }
}