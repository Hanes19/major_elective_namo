package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private ImageButton btnBack;
    private StudentScheduleAdapter adapter;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private String pendingRoomNavigation; // To store room if permission is requested

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Reusing the rooms list layout which contains a RecyclerView
        setContentView(R.layout.s_rooms_list);

        // Set the header title programmatically if possible, or ensure XML is generic
        TextView headerTitle = findViewById(R.id.header).findViewWithTag("headerTitle");
        if (headerTitle == null && findViewById(R.id.header) instanceof ViewGroup) {
            // Fallback: try to find the TextView inside the header include
            ViewGroup header = findViewById(R.id.header);
            for(int i=0; i<header.getChildCount(); i++) {
                if(header.getChildAt(i) instanceof TextView) {
                    ((TextView) header.getChildAt(i)).setText("My Schedule");
                    break;
                }
            }
        }

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadStudentSchedule();
    }

    private void loadStudentSchedule() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        int userId = dbHelper.getUserId(email);

        // Fetch the specific student schedule (enrolled classes)
        List<Schedule> schedules = dbHelper.getStudentSchedule(userId);

        if (schedules.isEmpty()) {
            Toast.makeText(this, "No classes enrolled yet.", Toast.LENGTH_SHORT).show();
        }

        adapter = new StudentScheduleAdapter(schedules);
        recyclerView.setAdapter(adapter);
    }

    // --- AR Navigation Logic ---
    private void checkPermissionAndNavigate(String roomName) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            pendingRoomNavigation = roomName;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startArNavigation(roomName);
        }
    }

    private void startArNavigation(String roomName) {
        Intent intent = new Intent(StudentScheduleActivity.this, StudentNavigationActivity.class);
        intent.putExtra("ROOM_NAME", roomName);
        startActivity(intent);
        // Optional: transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingRoomNavigation != null) {
                    startArNavigation(pendingRoomNavigation);
                }
            } else {
                Toast.makeText(this, "Camera permission needed for AR Navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --- Adapter Class ---
    private class StudentScheduleAdapter extends RecyclerView.Adapter<StudentScheduleAdapter.ViewHolder> {
        private List<Schedule> list;

        public StudentScheduleAdapter(List<Schedule> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Reusing item_room_row (contains tvRoomName and tvRoomDesc)
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Schedule s = list.get(position);

            // Display Subject as the main title
            holder.tvTitle.setText(s.getSubject());

            // Display Time and Room as details
            String details = s.getDay() + " (" + s.getStartTime() + " - " + s.getEndTime() + ")\n"
                    + "Room: " + s.getRoomName() + "\n"
                    + "Tap to Navigate ->";
            holder.tvDetails.setText(details);

            // Set Click Listener to launch AR Navigation
            holder.itemView.setOnClickListener(v -> {
                checkPermissionAndNavigate(s.getRoomName());
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDetails;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvRoomName);
                tvDetails = itemView.findViewById(R.id.tvRoomDesc);
            }
        }
    }
}