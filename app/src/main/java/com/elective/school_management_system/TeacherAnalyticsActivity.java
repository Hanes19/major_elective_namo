package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherAnalyticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvSubjectStats, tvSectionStats;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_analytics); // Ensure this XML exists

        dbHelper = new DatabaseHelper(this);

        // Find Views (Update IDs to match your actual XML)
        tvSubjectStats = findViewById(R.id.tvSubjectStats); // Placeholder ID
        tvSectionStats = findViewById(R.id.tvSectionStats); // Placeholder ID
        btnBack = findViewById(R.id.btnBack);

        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        loadSubjectStats();
        loadSectionStats();
    }

    private void loadSubjectStats() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        int teacherId = dbHelper.getUserId(email);

        Cursor cursor = dbHelper.getEnrollmentCountsForTeacher(teacherId);
        StringBuilder stats = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(0);
                int count = cursor.getInt(1);
                stats.append(subject).append(": ").append(count).append(" Students\n");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            stats.append("No enrollment data available.");
        }

        if(tvSubjectStats != null) tvSubjectStats.setText(stats.toString());
    }

    private void loadSectionStats() {
        Cursor cursor = dbHelper.getStudentCountsBySection();
        StringBuilder stats = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String section = cursor.getString(0);
                int count = cursor.getInt(1);
                stats.append("Section ").append(section).append(": ").append(count).append(" Students\n");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            stats.append("No section data available.");
        }

        if(tvSectionStats != null) tvSectionStats.setText(stats.toString());
    }
}