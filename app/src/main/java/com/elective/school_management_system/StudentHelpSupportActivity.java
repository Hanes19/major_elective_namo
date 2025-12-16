package com.elective.school_management_system;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StudentHelpSupportActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout btnTutorialAR, btnTutorialSearch;
    private LinearLayout btnEmailSupport, btnCallSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_help_support);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        btnTutorialAR = findViewById(R.id.btnTutorialAR);
        btnTutorialSearch = findViewById(R.id.btnTutorialSearch);
        btnEmailSupport = findViewById(R.id.btnEmailSupport);
        btnCallSupport = findViewById(R.id.btnCallSupport);
    }

    private void setupListeners() {
        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Tutorial: AR Navigation
        if (btnTutorialAR != null) {
            btnTutorialAR.setOnClickListener(v -> showTutorialDialog(
                    "AR Navigation",
                    "To use AR Navigation, tap the 'Navigation' tab at the bottom. Select a destination, and the camera will open with directional arrows pointing to your room. Ensure you are on campus for accurate results."
            ));
        }

        // Tutorial: Searching
        if (btnTutorialSearch != null) {
            btnTutorialSearch.setOnClickListener(v -> showTutorialDialog(
                    "Searching",
                    "You can search for rooms and faculty members in the Navigation List. Tap the search bar at the top of the list to filter results by name or room number."
            ));
        }

        // Support: Email
        if (btnEmailSupport != null) {
            btnEmailSupport.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:support@school.edu"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request - Student App");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Support: Call
        if (btnCallSupport != null) {
            btnCallSupport.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+631234567890"));
                startActivity(intent);
            });
        }
    }

    private void showTutorialDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Got it", null)
                .setIcon(android.R.drawable.ic_menu_help)
                .show();
    }
}