package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeacherAnalyticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout containerSubjectChart, containerSectionChart;
    private TextView tvTotalStudents, tvAvgClassSize;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_analytics);

        dbHelper = new DatabaseHelper(this);

        containerSubjectChart = findViewById(R.id.containerSubjectChart);
        containerSectionChart = findViewById(R.id.containerSectionChart);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvAvgClassSize = findViewById(R.id.tvAvgClassSize);
        btnBack = findViewById(R.id.btnBack);

        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        loadAnalytics();
    }

    private void loadAnalytics() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        int teacherId = dbHelper.getUserId(email);

        // 1. Enrollment by Subject
        Cursor cursorSub = dbHelper.getEnrollmentCountsForTeacher(teacherId);
        int totalEnrollment = 0;
        int subjectCount = 0;
        int maxSubjectVal = 0;
        List<DataPoint> subjectData = new ArrayList<>();

        if (cursorSub != null && cursorSub.moveToFirst()) {
            do {
                String subject = cursorSub.getString(0);
                int count = cursorSub.getInt(1);
                totalEnrollment += count;
                subjectCount++;
                if(count > maxSubjectVal) maxSubjectVal = count;
                subjectData.add(new DataPoint(subject, count));
            } while (cursorSub.moveToNext());
            cursorSub.close();
        }

        // 2. Students by Section
        Cursor cursorSec = dbHelper.getStudentCountsBySection();
        int maxSectionVal = 0;
        List<DataPoint> sectionData = new ArrayList<>();

        if (cursorSec != null && cursorSec.moveToFirst()) {
            do {
                String section = cursorSec.getString(0);
                int count = cursorSec.getInt(1);
                if(count > maxSectionVal) maxSectionVal = count;
                sectionData.add(new DataPoint("Section " + section, count));
            } while (cursorSec.moveToNext());
            cursorSec.close();
        }

        // 3. Update Summary Cards
        tvTotalStudents.setText(String.valueOf(totalEnrollment));
        int avg = (subjectCount > 0) ? totalEnrollment / subjectCount : 0;
        tvAvgClassSize.setText(String.valueOf(avg));

        // 4. Render Charts
        renderChart(containerSubjectChart, subjectData, maxSubjectVal, "#A9016D"); // Pink
        renderChart(containerSectionChart, sectionData, maxSectionVal, "#6200EE"); // Purple
    }

    private void renderChart(LinearLayout container, List<DataPoint> data, int maxValue, String colorHex) {
        container.removeAllViews();

        if(data.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No data available");
            empty.setTextColor(Color.LTGRAY);
            container.addView(empty);
            return;
        }

        for (DataPoint dp : data) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setPadding(0, 0, 0, 24);

            // Label & Value Row
            LinearLayout header = new LinearLayout(this);
            header.setOrientation(LinearLayout.HORIZONTAL);

            TextView lbl = new TextView(this);
            lbl.setText(dp.label);
            lbl.setTextColor(Color.WHITE);
            lbl.setTextSize(14);
            lbl.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));

            TextView val = new TextView(this);
            val.setText(String.valueOf(dp.value));
            val.setTextColor(Color.LTGRAY);
            val.setTextSize(14);

            header.addView(lbl);
            header.addView(val);
            row.addView(header);

            // Bar (ProgressBar)
            ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 24); // Height 24px
            params.topMargin = 12;
            bar.setLayoutParams(params);
            bar.setMax(maxValue > 0 ? maxValue : 100);
            bar.setProgress(dp.value);
            bar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(colorHex)));

            row.addView(bar);
            container.addView(row);
        }
    }

    private static class DataPoint {
        String label;
        int value;
        DataPoint(String l, int v) { label = l; value = v; }
    }
}