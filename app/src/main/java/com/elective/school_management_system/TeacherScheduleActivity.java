package com.elective.school_management_system;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TeacherScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private ImageButton btnBack;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Uses the shared layout containing a RecyclerView with ID 'recyclerViewRooms'
        setContentView(R.layout.s_rooms_list);

        // Update header title to "My Schedule"
        TextView tvHeader = findViewById(R.id.header).findViewById(R.id.btnBack).getNextFocusRightId() != -1
                ? null : findViewById(R.id.header).findViewWithTag("headerTitle");
        // Or cleaner: iterate children or just assume the textview is the 2nd child of header layout
        // For simplicity in this reuse scenario:
        TextView title = (TextView) ((ViewGroup)findViewById(R.id.header)).getChildAt(1);
        if(title != null) title.setText("My Schedule");

        dbHelper = new DatabaseHelper(this);

        // Init RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadSchedule();
    }

    private void loadSchedule() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        int userId = dbHelper.getUserId(email);

        List<Schedule> schedules = dbHelper.getUserSchedule(userId);

        // Use custom adapter
        adapter = new ScheduleAdapter(schedules);
        recyclerView.setAdapter(adapter);
    }

    // Internal Adapter Class
    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
        private List<Schedule> list;

        public ScheduleAdapter(List<Schedule> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Reusing item_room_row layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_row, parent, false);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
            Schedule s = list.get(position);
            // Mapping Schedule data to Room Row layout
            holder.tvSubject.setText(s.getSubject());
            String details = s.getDay() + " | " + s.getStartTime() + " - " + s.getEndTime() + "\n" + s.getRoomName();
            holder.tvDetails.setText(details);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ScheduleViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubject, tvDetails;

            public ScheduleViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSubject = itemView.findViewById(R.id.tvRoomName); // Reusing ID
                tvDetails = itemView.findViewById(R.id.tvRoomDesc); // Reusing ID
            }
        }
    }
}