package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class StudentResetPasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etOldPass, etNewPass, etConfirmPass;
    private ImageView btnToggleOld, btnToggleNew, btnToggleConfirm;
    private AppCompatButton btnSave;
    private DatabaseHelper dbHelper;
    private String currentEmail;

    // State for password visibility
    private boolean isOldVisible = false;
    private boolean isNewVisible = false;
    private boolean isConfirmVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_reset_pass);

        dbHelper = new DatabaseHelper(this);
        loadSession();
        initViews();
        setupListeners();
    }

    private void loadSession() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");
        if (currentEmail.isEmpty()) {
            Toast.makeText(this, "Session Error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        etOldPass = findViewById(R.id.etOldPassword);
        etNewPass = findViewById(R.id.etNewPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);

        btnToggleOld = findViewById(R.id.btnToggleOldPass);
        btnToggleNew = findViewById(R.id.btnToggleNewPass);
        btnToggleConfirm = findViewById(R.id.btnToggleConfirmPass);

        btnSave = findViewById(R.id.btnSavePassword); // Make sure you added this ID in XML!
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Toggle Visibility Logic
        btnToggleOld.setOnClickListener(v -> {
            isOldVisible = !isOldVisible;
            togglePass(etOldPass, isOldVisible);
        });
        btnToggleNew.setOnClickListener(v -> {
            isNewVisible = !isNewVisible;
            togglePass(etNewPass, isNewVisible);
        });
        btnToggleConfirm.setOnClickListener(v -> {
            isConfirmVisible = !isConfirmVisible;
            togglePass(etConfirmPass, isConfirmVisible);
        });

        // Save Logic
        btnSave.setOnClickListener(v -> attemptChangePassword());
    }

    private void togglePass(EditText editText, boolean isVisible) {
        if (isVisible) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.setSelection(editText.getText().length());
    }

    private void attemptChangePassword() {
        String oldPass = etOldPass.getText().toString().trim();
        String newPass = etNewPass.getText().toString().trim();
        String confirmPass = etConfirmPass.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Verify Old Password
        if (!dbHelper.checkUser(currentEmail, oldPass)) {
            Toast.makeText(this, "Incorrect Old Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Check Match
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Validate Complexity (1 Upper, 1 Lower, 1 Number)
        if (!isValidPassword(newPass)) {
            Toast.makeText(this, "Password must have 1 Upper, 1 Lower, and 1 Number", Toast.LENGTH_LONG).show();
            return;
        }

        // 4. Update DB
        boolean success = dbHelper.updatePassword(currentEmail, newPass);
        if (success) {
            Toast.makeText(this, "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPassword(String password) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }
}