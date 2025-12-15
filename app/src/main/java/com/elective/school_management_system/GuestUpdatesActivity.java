package com.elective.school_management_system;

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

import java.util.List;

public class GuestUpdatesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure you have a layout file named 't_update_list.xml' containing a RecyclerView
        setContentView(R.layout.t_update_list);

        ImageButton btnBack = findViewById(R.id.btnBack); // Assumes back button ID in layout is 'btnBack'
        if(btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        recyclerView = findViewById(R.id.recyclerUpdates); // Assumes RecyclerView ID is 'recyclerUpdates'
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            dbHelper = new DatabaseHelper(this);
            loadEvents();
        }
    }

    private void loadEvents() {
        List<Event> events = dbHelper.getAllEvents();
        EventAdapter adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);
    }

    // --- Inner Adapter Class ---
    private static class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private final List<Event> list;

        public EventAdapter(List<Event> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Using a simple built-in layout for the event items.
            // You can replace 'android.R.layout.simple_list_item_2' with a custom layout 'item_event_card.xml' if preferred.
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new EventViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event e = list.get(position);
            holder.title.setText(e.getTitle() + " (" + e.getDate() + ")");
            holder.desc.setText(e.getDescription());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class EventViewHolder extends RecyclerView.ViewHolder {
            TextView title, desc;
            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(android.R.id.text1);
                desc = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}