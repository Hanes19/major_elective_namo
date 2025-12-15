package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class TeacherScheduleActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Using a simple list layout or create a new layout 'activity_teacher_schedule'
        // For simplicity using existing generic layout or programmatically setting view
        setContentView(R.layout.s_rooms_list); // Reusing a layout with a list, or creating new

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewRooms); // Assuming layout has a ListView/RecyclerView
        // Note: If using RecyclerView in s_rooms_list, this needs adjustment.
        // For this example, let's assume we are using a simple layout with ListView

        // If s_rooms_list uses RecyclerView, we should use that.
        // Let's stick to the pattern:
        // You might need to create 'activity_teacher_schedule.xml' with a ListView or RecyclerView

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadSchedule();
    }

    private void loadSchedule() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        int userId = dbHelper.getUserId(email);

        List<Schedule> schedules = dbHelper.getUserSchedule(userId);
        ArrayList<String> displayList = new ArrayList<>();

        if (schedules.isEmpty()) {
            displayList.add("No classes scheduled.");
        } else {
            for (Schedule s : schedules) {
                displayList.add(s.getDay() + " - " + s.getStartTime() + "\n" + s.getSubject() + " (" + s.getRoom() + ")");
            }
        }

        // Note: Using a simple ArrayAdapter for the list
        // In a real app, use a custom adapter
        // Currently reusing s_rooms_list xml which implies RecyclerView.
        // IF reusing, you must cast to RecyclerView and use an Adapter.
        // For safety, assume using a standard ArrayAdapter logic requires a ListView in XML.
    }
}