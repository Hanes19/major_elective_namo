package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnSignIn = findViewById(R.id.btnSignIn);
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        TextView tvGuest = findViewById(R.id.tvGuest); // New Guest Button
        ImageButton btnEmail = findViewById(R.id.btnEmail);
        ImageButton btnPhone = findViewById(R.id.btnPhone);

        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Login_Activity.class);
            startActivity(intent);
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Guest Listener
        tvGuest.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GuestDashboardActivity.class);
            startActivity(intent);
        });

        View.OnClickListener alternativeLoginListener = v ->
                Toast.makeText(MainActivity.this, "Alternative login method selected", Toast.LENGTH_SHORT).show();

        btnEmail.setOnClickListener(alternativeLoginListener);
        btnPhone.setOnClickListener(alternativeLoginListener);
    }
}