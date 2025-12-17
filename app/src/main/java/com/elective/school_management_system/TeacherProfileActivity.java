package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeacherProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail, tvID;
    private TextView tvSubjectCount, tvStudentCount;
    private LinearLayout btnChangePassword, btnUpdates, btnHelp;
    private TextView btnLogout;
    private ImageButton btnBack, btnEdit;
    private LinearLayout navHome, navNav;

    private DatabaseHelper dbHelper;
    private String userEmail;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_profile);

        dbHelper = new DatabaseHelper(this);

        // Load Session
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", "");
        userId = dbHelper.getUserId(userEmail);

        if (userId == -1) {
            redirectToLogin();
            return;
        }

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data in case it was edited
        loadUserData();
        loadStatistics();
    }

    private void initViews() {
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail); // Using this for Department/Designation
        tvID = findViewById(R.id.tvID);
        tvSubjectCount = findViewById(R.id.tvSubjectCount);
        tvStudentCount = findViewById(R.id.tvStudentCount);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnUpdates = findViewById(R.id.btnUpdates);
        btnHelp = findViewById(R.id.btnHelp);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
    }

    private void loadUserData() {
        // 1. Load Name
        String username = dbHelper.getUsername(userEmail);
        tvProfileName.setText(username != null ? username : "Instructor");

        // 2. Load Profile Details
        Cursor cursor = dbHelper.getUserProfile(userId);
        if (cursor != null && cursor.moveToFirst()) {
            // Mappings based on TeacherEditProfileActivity logic:
            // course -> Department, year_level -> Designation, section -> Faculty ID
            String department = cursor.getString(cursor.getColumnIndexOrThrow("course"));
            String designation = cursor.getString(cursor.getColumnIndexOrThrow("year_level"));
            String facultyId = cursor.getString(cursor.getColumnIndexOrThrow("section"));

            // Format: "Professor, IT Department"
            String deptText = (department != null && !department.isEmpty()) ? department : "Department";
            String desigText = (designation != null && !designation.isEmpty()) ? designation : "Instructor";

            // Display Designation and Department combined
            tvProfileEmail.setText(desigText + ", " + deptText);

            tvID.setText("ID: " + (facultyId != null && !facultyId.isEmpty() ? facultyId : "--"));

            cursor.close();
        } else {
            tvProfileEmail.setText("Instructor");
            tvID.setText("ID: --");
        }
    }

    private void loadStatistics() {
        // 1. Calculate Subjects (Unique subjects in schedule)
        List<Schedule> scheduleList = dbHelper.getUserSchedule(userId);
        Set<String> uniqueSubjects = new HashSet<>();
        for (Schedule s : scheduleList) {
            uniqueSubjects.add(s.getSubject());
        }
        tvSubjectCount.setText(String.valueOf(uniqueSubjects.size()));

        // 2. Calculate Total Students (Sum of enrollments per subject)
        int totalStudents = 0;
        Cursor enrollCursor = dbHelper.getEnrollmentCountsForTeacher(userId);
        if (enrollCursor != null) {
            if (enrollCursor.moveToFirst()) {
                do {
                    totalStudents += enrollCursor.getInt(enrollCursor.getColumnIndexOrThrow("count"));
                } while (enrollCursor.moveToNext());
            }
            enrollCursor.close();
        }
        tvStudentCount.setText(String.valueOf(totalStudents));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherEditProfileActivity.class));
        });

        btnLogout.setOnClickListener(v -> logoutUser());

        // Menu Items
        btnChangePassword.setOnClickListener(v -> Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show());

        btnUpdates.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherUpdatesActivity.class));
        });

        btnHelp.setOnClickListener(v -> Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show());

        // Bottom Navigation
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        navNav.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherRoomsActivity.class));
            finish();
        });
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        prefs.edit().clear().apply();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}